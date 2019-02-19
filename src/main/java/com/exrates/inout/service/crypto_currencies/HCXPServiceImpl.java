package com.exrates.inout.service.crypto_currencies;


import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.utils.WithdrawUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import wallet.MoneroTransaction;
import wallet.MoneroWalletRpc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.exrates.inout.domain.main.Currency;

public class HCXPServiceImpl implements MoneroService {

    private MoneroWalletRpc wallet;

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

    private String mainAccount;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static final int INTEGRATED_ADDRESS_DIGITS = 16;

    private Logger log;

    public HCXPServiceImpl(String propertySource, String merchantName, String currencyName, Integer minConfirmations, Integer decimals) {

        Properties props = new Properties();

        try {
            props.load(getClass().getClassLoader().getResourceAsStream(propertySource));
            this.HOST = props.getProperty("monero.host");
            this.PORT = props.getProperty("monero.port");
            this.LOGIN = props.getProperty("monero.login");
            this.PASSWORD = props.getProperty("monero.password");
            this.MODE = props.getProperty("monero.mode");
            this.mainAccount = props.getProperty("monero.mainAccount");
            this.merchantName = merchantName;
            this.currencyName = currencyName;
            this.minConfirmations = minConfirmations;
            this.decimals = decimals;
            this.log = LogManager.getLogger(props.getProperty("monero.log"));
        } catch (IOException e) {
            log.error(e);
        }

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
    public void init(){

        currency = currencyService.findByName(currencyName);
        merchant = merchantService.findByName(merchantName);

        ADDRESSES = refillService.findAllAddresses(merchant.getId(), currency.getId());

        if (MODE.equals("main")){
            log.info(merchantName + " starting...");
            try {
                wallet = new MoneroWalletRpc(HOST, Integer.parseInt(PORT), LOGIN, PASSWORD);
                log.info(merchantName + " started");
                scheduler.scheduleAtFixedRate(() -> {
                    {
                        checkIncomingTransactions();
                    }
                }, 3, 30, TimeUnit.MINUTES);
            }catch (Exception e){
                log.error(e);
            }
        }else {
            log.info(merchantName + " test mode...");
        }
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Override
    public void sendToMainAccount() {
        try {
            log.info("Starting sendToMainAccount");
            BigInteger unlockedBalance = wallet.getUnlockedBalance();
            BigInteger currentFee = new BigInteger("1000000");
            BigInteger amountToSend = unlockedBalance.subtract(currentFee);
            if (amountToSend.compareTo(new BigInteger("0")) <= 0) {
                log.info("No money for sending..");
                return;
            }
            log.info("Balance from node " + wallet.getUnlockedBalance() + ", amount to send with comission = " + amountToSend);

            //TODO remove after successfully withdraw
            String mainAccountVerification = "hysGeMWsvZdBq7tBimycV71rXSwJPkn4f2gRNfDwP2AogUK7cNYt7saCAcYA9cFsvpeUEiiYA44jm1GRswFENhan2XEaiEfMo";
            if(!mainAccount.equals(mainAccountVerification)){
                log.error("Unexpected main account address, expected " + mainAccountVerification + ", but was " + mainAccount);
                return;
            }
            MoneroTransaction transaction = wallet.send(mainAccount, amountToSend, "", 0, 10);
            log.info(transaction);
        }catch (Throwable e){
            log.error(e);
        }
    }

    private void checkIncomingTransactions(){
        try {
            log.info(merchantName + ": Checking transactions...");
            log.info(new Date());
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
