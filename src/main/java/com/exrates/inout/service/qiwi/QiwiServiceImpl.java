package com.exrates.inout.service.qiwi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseTransaction;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//exrates.model.Currency;
//exrates.model.Merchant;
//exrates.model.dto.RefillRequestAcceptDto;
//exrates.model.dto.RefillRequestCreateDto;
//exrates.model.dto.WithdrawMerchantOperationDto;
//exrates.model.dto.qiwi.response.QiwiResponseTransaction;
//exrates.service.CurrencyService;
//exrates.service.GtagService;
//exrates.service.MerchantService;
//exrates.service.RefillService;
//exrates.service.exception.RefillRequestAppropriateNotFoundException;

//@Log4j2(topic = "Qiwi")
@Service
@PropertySource("classpath:/merchants/qiwi.properties")
@Profile("!dev")
public class QiwiServiceImpl implements QiwiService {

   private static final Logger log = LogManager.getLogger("Qiwi");


    private final static String MERCHANT_NAME = "QIWI";
    private final static String CURRENCY_NAME = "RUB";

    private static final Logger logger = org.apache.logging.log4j.LogManager.getLogger(QiwiServiceImpl.class);

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private QiwiExternalService qiwiExternalService;
    @Autowired
    private GtagService gtagService;

    private Merchant merchant;
    private Currency currency;

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(CURRENCY_NAME);
        merchant = merchantService.findByName(MERCHANT_NAME);
    }

    @Value("${qiwi.account.address}")
    private String mainAddress;

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

        logger.debug("Process of sending data to Google Analytics...");
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
