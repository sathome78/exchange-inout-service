package com.exrates.inout.service.qiwi;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseTransaction;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.QiwiProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import lombok.Synchronized;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//@Log4j2(topic = "Qiwi")
@Service
@Profile("!dev")
public class QiwiServiceImpl implements QiwiService {

    private static final Logger log = org.apache.logging.log4j.LogManager.getLogger(QiwiServiceImpl.class);

    private final static String MERCHANT_NAME = "QIWI";
    private final static String CURRENCY_NAME = "RUB";

    private MerchantService merchantService;
    private CurrencyService currencyService;
    private RefillService refillService;
    private MessageSource messageSource;
    private QiwiExternalService qiwiExternalService;
    private GtagService gtagService;

    private String mainAddress;

    private Merchant merchant;
    private Currency currency;

    @Autowired
    public QiwiServiceImpl(MerchantService merchantService, CurrencyService currencyService, RefillService refillService,
                           MessageSource messageSource, QiwiExternalService qiwiExternalService, GtagService gtagService,
                           CryptoCurrencyProperties cryptoCurrencyProperties){
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.refillService = refillService;
        this.messageSource = messageSource;
        this.qiwiExternalService = qiwiExternalService;
        this.gtagService = gtagService;

        QiwiProperty qiwiProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getQiwi();
        mainAddress = qiwiProperty.getAccountAddress();
    }

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(CURRENCY_NAME);
        merchant = merchantService.findByName(MERCHANT_NAME);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String destinationTag = qiwiExternalService.generateUniqMemo(request.getUserId());
        String message = messageSource.getMessage("merchants.refill.qiwi",
                new Object[]{mainAddress, destinationTag}, request.getLocale());
        return new HashMap<String, String>() {{
            put("address", destinationTag);
            put("message", message);
            put("qr", mainAddress);
        }};
    }

    @Override
    public String getMainAddress() {
        return mainAddress;
    }

    @Synchronized
    @Override
    public void onTransactionReceive(QiwiResponseTransaction transaction, String amount, String currencyName, String merchant) {
        log.info("*** Qiwi *** Income transaction {} ", transaction.getNote() + " " + amount);
        if (checkTransactionForDuplicate(transaction)) {
            log.warn("*** Qiwi *** transaction {} already accepted", transaction.get_id());
            return;
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("hash", transaction.get_id());
        String memo = transaction.getNote().substring(transaction.getNote().indexOf(":") + 1);
        if (memo == null) {
            log.warn("*** Qiwi *** Memo is null");
            return;
        }
        paramsMap.put("currency", currencyName);
        paramsMap.put("merchant", merchant);
        paramsMap.put("address", memo);
        paramsMap.put("amount", amount);
        try {
            this.processPayment(paramsMap);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error("*** Qiwi *** refill address not found {}", transaction);
        }
    }

    private boolean checkTransactionForDuplicate(QiwiResponseTransaction transaction) {
        return StringUtils.isEmpty(transaction.getNote()) || StringUtils.isEmpty(transaction.get_id()) || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchant.getId(), currency.getId(),
                transaction.get_id()).isPresent();
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String address = params.get("address");
        String hash = params.get("hash");
        Currency currency = currencyService.findByName(params.get("currency"));
        Merchant merchant = merchantService.findByName(MERCHANT_NAME);
        BigDecimal fullAmount = new BigDecimal(params.get("amount"));

        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(fullAmount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();

        Integer requestId;
        try {
            requestId = refillService.getRequestId(requestAcceptDto);
            requestAcceptDto.setRequestId(requestId);

            refillService.autoAcceptRefillRequest(requestAcceptDto);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.debug("RefillRequestNotFountException: " + params);
            requestId = refillService.createRefillRequestByFact(requestAcceptDto);
            requestAcceptDto.setRequestId(requestId);

            refillService.autoAcceptRefillRequest(requestAcceptDto);
        }
        final String username = refillService.getUsernameByRequestId(requestId);

        log.debug("Process of sending data to Google Analytics...");
        gtagService.sendGtagEvents(fullAmount.toString(), currency.getName(), username);
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        throw new RuntimeException("Not implemented for qiwi.");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return !address.equals(mainAddress);
    }
}
