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
import com.exrates.inout.properties.models.LiskNode;
import com.exrates.inout.properties.models.LiskProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.util.ParamMapUtils;
import com.exrates.inout.util.WithdrawUtils;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


//@Log4j2(topic = "lisk_log")
public class LiskServiceImpl implements LiskService {

   private static final Logger log = LogManager.getLogger("lisk_log");


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
    @Autowired
    private GtagService gtagService;

    private LiskRestClient liskRestClient;

    private LiskSpecialMethodService liskSpecialMethodService;

    private final String merchantName;
    private final String currencyName;
    private LiskNode liskNodeProperties;
    private String mainAddress;
    private String mainSecret;
    private Integer minConfirmations;

    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    public LiskServiceImpl(LiskRestClient liskRestClient, LiskSpecialMethodService liskSpecialMethodService, String merchantName, String currencyName, LiskProperty liskProperty) {
        this.liskRestClient = liskRestClient;
        this.liskSpecialMethodService = liskSpecialMethodService;
        this.merchantName = merchantName;
        this.currencyName = currencyName;
        this.liskNodeProperties = liskProperty.getNode();
        this.mainAddress = liskProperty.getNode().getAddress();
        this.mainSecret = liskProperty.getNode().getSecret();
        this.minConfirmations = liskProperty.getMinConfirmations();
    }

    @PostConstruct
    private void init() {
        liskRestClient.initClient(liskNodeProperties);
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
        log.info("ApolloServiceImpl.processPayment() start method.................");
        Optional<String> refillRequestIdResult = Optional.ofNullable(params.get("requestId"));
        Integer currencyId = Integer.parseInt(ParamMapUtils.getIfNotNull(params, "currencyId"));
        Integer merchantId = Integer.parseInt(ParamMapUtils.getIfNotNull(params, "merchantId"));
        String address = ParamMapUtils.getIfNotNull(params, "address");
        String txId = ParamMapUtils.getIfNotNull(params, "txId");
        LiskTransaction transaction = getTransactionById(txId);
        log.info("BEFORE liskRestClient.getFee() .................");
        long txFee = liskRestClient.getFee();
        BigDecimal scaledAmount = LiskTransaction.scaleAmount(transaction.getAmount() - txFee);

        if (!refillRequestIdResult.isPresent()) {
            log.info("BEFORE ---  refillService.createRefillRequestByFact(RefillRequestAcceptDto.........)");
            Integer requestId = refillService.createRefillRequestByFact(RefillRequestAcceptDto.builder()
                    .address(address)
                    .amount(scaledAmount)
                    .merchantId(merchantId)
                    .currencyId(currencyId)
                    .merchantTransactionId(txId).build());
            log.info("AFTER ---  refillService.createRefillRequestByFact(RefillRequestAcceptDto.........)");
            if (transaction.getConfirmations() >= 0 && transaction.getConfirmations() < minConfirmations) {
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
                    log.info("AFTER ---  refillService.putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto.builder().........)");
                } catch (RefillRequestAppropriateNotFoundException e) {
                    log.error(e + " in processPayment() method.......");
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
        log.info("END OF LiskServiceImpl.processPayment().........");
    }

    private void changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto dto) {
        log.info("changeConfirmationsOrProvide start.........)");
        try {
            refillService.setConfirmationCollectedNumber(dto);
            if (dto.getConfirmations() >= minConfirmations) {
                log.debug("Providing transaction!");
                Integer requestId = dto.getRequestId();

                log.info("BEFORE RefillRequestAcceptDto.builder().........)");
                RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                        .address(dto.getAddress())
                        .amount(dto.getAmount())
                        .currencyId(dto.getCurrencyId())
                        .merchantId(dto.getMerchantId())
                        .merchantTransactionId(dto.getHash())
                        .build();

                if (Objects.isNull(requestId)) {
                    requestId = refillService.getRequestId(requestAcceptDto);
                }
                requestAcceptDto.setRequestId(requestId);
                log.info("BEFORE ---  refillService.autoAcceptRefillRequest(requestAcceptDto)");
                refillService.autoAcceptRefillRequest(requestAcceptDto);
                log.info("AFTER ---  refillService.autoAcceptRefillRequest(requestAcceptDto)");
                RefillRequestFlatDto flatDto = refillService.getFlatById(requestId);
                sendTransaction(flatDto.getBrainPrivKey(), dto.getAmount(), mainAddress);

                final String username = refillService.getUsernameByRequestId(requestId);

                log.debug("Process of sending data to Google Analytics...");
                gtagService.sendGtagEvents(requestAcceptDto.getAmount().toString(), currencyName, username);
            }
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error(e);
        }
    }


    @Override
    public void processTransactionsForKnownAddresses() {
        log.info("Start checking {} transactions", currencyName);
        Currency currency = currencyService.findByName(currencyName);
        Merchant merchant = merchantService.findByName(merchantName);
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
                log.error("Exception for currency {} merchant {}: {}", currencyName, merchantName, ExceptionUtils.getStackTrace(e));
            }
        });
    }


    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        if (!"LSK".equalsIgnoreCase(withdrawMerchantOperationDto.getCurrency())) {
            throw new WithdrawRequestPostException("Currency not supported by merchant");
        }
        BigDecimal txFee = LiskTransaction.scaleAmount(liskRestClient.getFee());
        if (StringUtils.isEmptyOrWhitespaceOnly(mainSecret)) {
            throw new WithdrawRequestPostException("Main secret not defined");
        }
        String txId = sendTransaction(mainSecret, new BigDecimal(withdrawMerchantOperationDto.getAmount()).subtract(txFee),
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
