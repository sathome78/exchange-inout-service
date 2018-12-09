package com.exrates.inout.service.crypto_currencies;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.models.MoneroProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.utils.WithdrawUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Transactional;
import wallet.MoneroTransaction;
import wallet.MoneroWallet;
import wallet.MoneroWalletRpc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "monero.log")
public class MoneroServiceImpl implements MoneroService {

    private MoneroWallet wallet;

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private WithdrawUtils withdrawUtils;

    private String HOST;
    private String PORT;
    private String LOGIN;
    private String PASSWORD;
    private String MODE;

    private List<String> ADDRESSES = new ArrayList<>();

    private Merchant merchant;
    private Currency currency;
    private String merchantName;
    private String currencyName;
    private Integer minConfirmations;
    private Integer decimals;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final int INTEGRATED_ADDRESS_DIGITS = 16;

    public MoneroServiceImpl(MoneroProperty property) {
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.minConfirmations = property.getMinConfirmations();
        this.decimals = property.getDecimals();
        this.HOST = property.getNode().getHost();
        this.PORT = property.getNode().getPort();
        this.LOGIN = property.getNode().getLogin();
        this.PASSWORD = property.getNode().getPassword();
        this.MODE = property.getNode().getMode();
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        return new HashMap<>();
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {

        BigDecimal amount = new BigDecimal(params.get("amount"));

        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(params.get("address"))
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(params.get("hash"))
                .build();

        Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
        requestAcceptDto.setRequestId(requestId);
        refillService.autoAcceptRefillRequest(requestAcceptDto);
    }

    @Override
    @Transactional
    public Map<String, String> refill(RefillRequestCreateDto request) {

        Map<String, String> mapAddress = new HashMap<>();
        String address = StringUtils.EMPTY;
        String pubKey = StringUtils.EMPTY;
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
            Date date = new Date();
            StringBuilder dateString = new StringBuilder(dateFormat.format(date) + String.valueOf(request.getUserId()));
            for (int i = dateString.length(); i < INTEGRATED_ADDRESS_DIGITS; i++) {
                dateString.append("0");
            }
            pubKey = dateString.toString();
            address = String.valueOf(wallet.getIntegratedAddress(dateString.toString()));
            ADDRESSES.add(address);

        } catch (Exception e) {
            log.error(e);
        }
        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[]{address}, request.getLocale());

        mapAddress.put("message", message);
        mapAddress.put("address", address);
        mapAddress.put("qr", mapAddress.get("address"));
        mapAddress.put("pubKey", pubKey);

        return mapAddress;
    }

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(currencyName);
        merchant = merchantService.findByName(merchantName);

        ADDRESSES = refillService.findAllAddresses(merchant.getId(), currency.getId());
        System.out.println("init monero");
        if (MODE.equals("main")) {
            log.info(merchantName + " starting...");
            try {
                wallet = new MoneroWalletRpc(HOST, Integer.parseInt(PORT), LOGIN, PASSWORD);
                log.info(merchantName + " started");
                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        checkIncomingTransactions();
                    }
                }, 3, 60, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            log.info(merchantName + " test mode...");
        }
    }

    private void checkIncomingTransactions() {
        try {
            log.info(merchantName + ": Checking transactions...");
            log.info(new Date());
            HashMap<String, String> mapAddresses = new HashMap<>();
            Set<String> payments = new HashSet<>();

            log.info(ADDRESSES.toString());
            for (String address : ADDRESSES) {
                log.info(address);
                String paymentId = wallet.splitIntegratedAddress(address).getPaymentId();
                mapAddresses.put(paymentId, address);
            }

            List<MoneroTransaction> transactions = wallet.getTransactions(true, false, false, false, false, mapAddresses.keySet(), null, null);
            for (MoneroTransaction transaction : transactions) {
                try {
                    log.info(transaction.toString());
                    String integratedAddress = mapAddresses.get(transaction.getPaymentId());
                    log.info("integratedAddress: " + integratedAddress);
                    log.info(refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(integratedAddress, merchant.getId(), currency.getId(), transaction.getHash()));
                    if ((transaction.getType().equals("INCOMING")) || !transaction.getUnlockTime().equals(0)
                            || refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(integratedAddress, merchant.getId(), currency.getId()
                            , transaction.getHash()).isPresent() || (!ADDRESSES.contains(integratedAddress))) {
                        continue;
                    }
                    int confirmations = wallet.getHeight() - transaction.getHeight();
                    log.info("confirmations:" + confirmations);
                    if (confirmations < minConfirmations) {
                        continue;
                    }
                    Double amount = transaction.getPayments().get(0).getAmount().doubleValue() / Math.pow(10.0D, (double) decimals);

                    Map<String, String> mapPayment = new HashMap<>();
                    mapPayment.put("address", integratedAddress);
                    mapPayment.put("hash", transaction.getHash());
                    mapPayment.put("amount", String.valueOf(amount));

                    processPayment(mapPayment);
                } catch (Exception e) {
                    log.error(e);
                }
            }
            log.info(new Date());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }

    @PreDestroy
    private void shutdown() {
        log.debug("Destroying " + merchantName);
        scheduler.shutdown();
        log.debug("Destroyed " + merchantName);
    }
}
