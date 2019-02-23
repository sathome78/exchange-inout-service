package com.exrates.inout.service.monero;

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
import com.exrates.inout.util.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MoneroServiceImpl implements MoneroService {

    private static final int INTEGRATED_ADDRESS_DIGITS = 16;

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

    private MoneroWallet wallet;

    private String host;
    private String port;
    private String login;
    private String password;
    private String mode;
    private Logger log;

    private List<String> ADDRESSES = new ArrayList<>();

    private Merchant merchant;
    private Currency currency;
    private String merchantName;
    private String currencyName;
    private Integer minConfirmations;
    private Integer decimals;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public MoneroServiceImpl(MoneroProperty property) {
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.minConfirmations = property.getMinConfirmations();
        this.decimals = property.getDecimals();
        this.host = property.getNode().getHost();
        this.port = property.getNode().getPort();
        this.login = property.getNode().getLogin();
        this.password = property.getNode().getPassword();
        this.mode = property.getNode().getMode();
        this.log = LogManager.getLogger(property.getNode().getLog());
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
        String address = "";
        String pubKey = "";
        try {
            DateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
            Date date = new Date();
            String dateString = dateFormat.format(date) + String.valueOf(request.getUserId());
            for (int i=dateString.length(); i<INTEGRATED_ADDRESS_DIGITS; i++){
                dateString += "0";
            }
            pubKey = dateString;
            address = String.valueOf(wallet.getIntegratedAddress(dateString));
            ADDRESSES.add(address);

        } catch (Exception e) {
            //log.error(e);
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
    public void init(){

        currency = currencyService.findByName(currencyName);
        merchant = merchantService.findByName(merchantName);

        ADDRESSES = refillService.findAllAddresses(merchant.getId(), currency.getId());

        if (mode.equals("main")){
            log.info(merchantName + " starting...");
            try {
                wallet = new MoneroWalletRpc(host, Integer.parseInt(port), login, password);
                log.info(merchantName + " started");
                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        checkIncomingTransactions();
                    }
                }, 3, 60, TimeUnit.MINUTES);
            }catch (Exception e){
                //log.error(e);
            }
        }else {
            log.info(merchantName + " test mode...");
        }
    }

    private void checkIncomingTransactions(){
        try {
            log.info(merchantName + ": Checking transactions...");
            log.info(new java.util.Date());
            HashMap<String,String> mapAddresses = new HashMap<>();
            Set<String> payments = new HashSet<>();

            log.info(ADDRESSES.toString());
            for (String address : ADDRESSES){
                log.info(address.toString());
                String paymentId = wallet.splitIntegratedAddress(address).getPaymentId();
                mapAddresses.put(paymentId, address);
            }

            List<MoneroTransaction> transactions = wallet.getTransactions(true, false, false, false, false, mapAddresses.keySet(), (Integer)null, (Integer)null);
            for (MoneroTransaction transaction : transactions){
                try {
                    log.info(transaction.toString());
                    String integratedAddress = mapAddresses.get(transaction.getPaymentId());
                    log.info("integratedAddress: " + integratedAddress);
                    log.info(refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(integratedAddress,merchant.getId(),currency.getId(),transaction.getHash()));
                    if ((transaction.getType().equals("INCOMING")) || !transaction.getUnlockTime().equals(0)
                            || refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(integratedAddress,merchant.getId(),currency.getId()
                            ,transaction.getHash()).isPresent() || (!ADDRESSES.contains(integratedAddress))){
                        continue;
                    }
                    int confirmations = wallet.getHeight() - transaction.getHeight();
                    log.info("confirmations:" + confirmations);
                    if (confirmations < minConfirmations){
                        continue;
                    }

                    Double amount = transaction.getPayments().get(0).getAmount().doubleValue()/ Math.pow(10.0D, (double)decimals);

                    Map<String, String> mapPayment = new HashMap<>();
                    mapPayment.put("address", integratedAddress);
                    mapPayment.put("hash", transaction.getHash());
                    mapPayment.put("amount", String.valueOf(amount));

                    processPayment(mapPayment);
                }catch (Exception e){
                    //log.error(e);
                }
            }

            log.info(new java.util.Date());

        } catch (Exception e) {
            //log.error(e);
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
