package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.RefillRequestPutOnBchExamDto;
import com.exrates.inout.domain.dto.RefillRequestSetConfirmationsNumberDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.domain.lisk.LiskTransaction;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.LiskCreateAddressException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.WithdrawRequestPostException;
import com.exrates.inout.properties.models.LiskProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.utils.WithdrawUtils;
import com.exrates.inout.util.ParamMapUtils;
import com.mysql.jdbc.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "lisk_log")
@Builder(builderClassName = "Builder")
@NoArgsConstructor
@AllArgsConstructor
public class LiskServiceImpl implements LiskService {

    private final BigDecimal DEFAULT_LSK_TX_FEE = BigDecimal.valueOf(0.1);

    @Autowired
    private RefillService refillService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WithdrawUtils withdrawUtils;

    private LiskRestClient liskRestClient;
    private LiskSpecialMethodService liskSpecialMethodService;

    private LiskProperty property;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @PostConstruct
    private void init() {
        liskRestClient.initClient(property);
        scheduler.scheduleAtFixedRate(this::processTransactionsForKnownAddresses, 3L, 30L, TimeUnit.MINUTES);
    }

    @PreDestroy
    private void shutdown() {
        scheduler.shutdown();
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        try {
            String secret = String.join(" ", MnemonicCode.INSTANCE.toMnemonic(SecureRandom.getSeed(16)));
            LiskAccount account = createNewLiskAccount(secret);
            String address = account.getAddress();

            String message = messageSource.getMessage("merchants.refill.btc",
                    new Object[]{address}, request.getLocale());
            Map<String, String> result = new HashMap<String, String>() {{
                put("message", message);
                put("address", address);
                put("pubKey", account.getPublicKey());
                put("brainPrivKey", secret);
                put("qr", address);
            }};
            return result;
        } catch (MnemonicException.MnemonicLengthException e) {
            throw new LiskCreateAddressException(e);
        }
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Optional<String> refillRequestIdResult = Optional.ofNullable(params.get("requestId"));
        Integer currencyId = Integer.parseInt(ParamMapUtils.getIfNotNull(params, "currencyId"));
        Integer merchantId = Integer.parseInt(ParamMapUtils.getIfNotNull(params, "merchantId"));
        String address = ParamMapUtils.getIfNotNull(params, "address");
        String txId = ParamMapUtils.getIfNotNull(params, "txId");
        LiskTransaction transaction = getTransactionById(txId);
        long txFee = liskRestClient.getFee();
        BigDecimal scaledAmount = LiskTransaction.scaleAmount(transaction.getAmount() - txFee);

        if (!refillRequestIdResult.isPresent()) {
            Integer requestId = refillService.createRefillRequestByFact(RefillRequestAcceptDto.builder()
                    .address(address)
                    .amount(scaledAmount)
                    .merchantId(merchantId)
                    .currencyId(currencyId)
                    .merchantTransactionId(txId).build());
            if (transaction.getConfirmations() >= 0 && transaction.getConfirmations() < property.getMinConfirmations()) {
                try {
                    log.debug("put on bch exam {}", transaction);
                    refillService.putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto.builder()
                            .requestId(requestId)
                            .merchantId(merchantId)
                            .currencyId(currencyId)
                            .address(address)
                            .amount(scaledAmount)
                            .hash(txId)
                            .blockhash(transaction.getBlockId()).build());
                } catch (RefillRequestAppropriateNotFoundException e) {
                    log.error(e);
                }
            } else {
                changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                        .requestId(requestId)
                        .address(address)
                        .amount(scaledAmount)
                        .confirmations(transaction.getConfirmations())
                        .currencyId(currencyId)
                        .merchantId(merchantId)
                        .hash(txId)
                        .blockhash(transaction.getBlockId()).build());
            }
        } else {
            Integer requestId = Integer.parseInt(refillRequestIdResult.get());
            changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                    .requestId(requestId)
                    .address(address)
                    .amount(scaledAmount)
                    .confirmations(transaction.getConfirmations())
                    .currencyId(currencyId)
                    .merchantId(merchantId)
                    .hash(txId)
                    .blockhash(transaction.getBlockId()).build());
        }
    }

    private void changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto dto) {
        try {
            refillService.setConfirmationCollectedNumber(dto);
            if (dto.getConfirmations() >= property.getMinConfirmations()) {
                log.debug("Providing transaction!");
                RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.of(dto);
                refillService.autoAcceptRefillRequest(requestAcceptDto);
                RefillRequestFlatDto flatDto = refillService.getFlatById(dto.getRequestId());
                sendTransaction(flatDto.getBrainPrivKey(), dto.getAmount(), property.getNode().getAddress());
            }
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error(e);
        }
    }

    @Override
    public void processTransactionsForKnownAddresses() {
        log.info("Start checking {} transactions", property.getCurrencyName());
        Currency currency = currencyService.findByName(property.getCurrencyName());
        Merchant merchant = merchantService.findByName(property.getMerchantName());
        refillService.findAllAddresses(merchant.getId(), currency.getId()).forEach(address -> {
            try {
                int offset = refillService.getTxOffsetForAddress(address);
                List<LiskTransaction> userTransactions = liskRestClient.getAllTransactionsByRecipient(address, offset);
                log.debug("Address {}, Transactions found: {}", address, userTransactions);
                boolean containsUnconfirmedTransactions = false;
                int newOffset = offset;
                for (LiskTransaction transaction : userTransactions) {
                    Optional<RefillRequestFlatDto> refillRequestResult = refillService.findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(transaction.getRecipientId(),
                            merchant.getId(), currency.getId(), transaction.getId());
                    if ((refillRequestResult.isPresent() && refillRequestResult.get().getStatus().isSuccessEndStatus())) {
                        if (!containsUnconfirmedTransactions) {
                            newOffset++;
                        }
                    } else {
                        if (!containsUnconfirmedTransactions) {
                            containsUnconfirmedTransactions = true;
                        }
                        Map<String, String> params = new HashMap<String, String>() {{
                            put("merchantId", String.valueOf(merchant.getId()));
                            put("currencyId", String.valueOf(currency.getId()));
                            put("address", transaction.getRecipientId());
                            put("txId", transaction.getId());
                        }};
                        refillRequestResult.ifPresent(request -> params.put("requestId", String.valueOf(request.getId())));

                        try {
                            processPayment(params);
                        } catch (RefillRequestAppropriateNotFoundException e) {
                            log.error(e);
                        }
                    }
                }
                if (newOffset != offset) {
                    refillService.updateTxOffsetForAddress(address, newOffset);
                }
            } catch (Exception e) {
                log.error("Exception for currency {} merchant {}: {}", property.getCurrencyName(), property.getMerchantName(), ExceptionUtils.getStackTrace(e));
            }
        });
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        if (!"LSK".equalsIgnoreCase(withdrawMerchantOperationDto.getCurrency())) {
            throw new WithdrawRequestPostException("Currency not supported by merchant");
        }
        BigDecimal txFee = LiskTransaction.scaleAmount(liskRestClient.getFee());
        if (StringUtils.isEmptyOrWhitespaceOnly(property.getNode().getSecret())) {
            throw new WithdrawRequestPostException("Main secret not defined");
        }
        String txId = sendTransaction(property.getNode().getSecret(), new BigDecimal(withdrawMerchantOperationDto.getAmount()).subtract(txFee),
                withdrawMerchantOperationDto.getAccountTo());
        return Collections.singletonMap("hash", txId);
    }

    @Override
    public LiskTransaction getTransactionById(String txId) {
        return liskRestClient.getTransactionById(txId);
    }

    @Override
    public List<LiskTransaction> getTransactionsByRecipient(String recipientAddress) {
        return liskRestClient.getTransactionsByRecipient(recipientAddress);
    }

    @Override
    public String sendTransaction(String secret, Long amount, String recipientId) {
        return liskSpecialMethodService.sendTransaction(secret, amount, recipientId);
    }

    @Override
    public String sendTransaction(String secret, BigDecimal amount, String recipientId) {
        return sendTransaction(secret, LiskTransaction.unscaleAmountToLiskFormat(amount), recipientId);
    }

    @Override
    public LiskAccount createNewLiskAccount(String secret) {
        return liskSpecialMethodService.createAccount(secret);
    }

    @Override
    public LiskAccount getAccountByAddress(String address) {
        return liskRestClient.getAccountByAddress(address);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }
}
