package com.exrates.inout.service.zil.impl;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.zil.ZilCurrencyService;
import com.exrates.inout.service.zil.ZilService;
import com.exrates.inout.util.WithdrawUtils;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Log4j2(topic = "zil_log")
@Service
public class ZilServiceImpl implements ZilService {

    private static final String CURRENCY_NAME = "ZIL";
    private static final String CODE_FROM_AWS = "zil_coin\":\"";
    private Merchant merchant;
    private Currency currency;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WithdrawUtils withdrawUtils;
    @Autowired
    private ZilCurrencyService zilCurrencyService;
    @Autowired
    private AlgorithmService algorithmService;

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(CURRENCY_NAME);
        merchant = merchantService.findByName(CURRENCY_NAME);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String privKey = zilCurrencyService.generatePrivateKey();
        String address = zilCurrencyService.getAddressFromPrivateKey(privKey);
        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[] {address}, request.getLocale());
        return new HashMap<String, String>(){{
            put("privKey", algorithmService.encodeByKey(CODE_FROM_AWS, privKey));
            put("pubKey", zilCurrencyService.getPublicKeyFromPrivateKey(privKey));
            put("address", address);
            put("message", message);
            put("qr", address);
        }};
    }

    @Synchronized
    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String address = params.get("address");
        String hash = params.get("hash");
        BigDecimal amount = new BigDecimal(params.get("amount"));
        BigDecimal fee = zilCurrencyService.getFee();
        BigDecimal scaledAmount = zilCurrencyService.scaleAmountToZilFormat(amount.subtract(fee));

        if (checkTransactionForDuplicate(hash)) {
            log.warn("*** zil *** transaction {} already accepted", hash);
            return;
        }

        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(address)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(scaledAmount)
                .merchantTransactionId(hash)
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();

        refillService.createAndAutoAcceptRefillRequest(requestAcceptDto);
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        throw new RuntimeException("Not supported");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }

    private boolean checkTransactionForDuplicate(String hash){
        return StringUtils.isEmpty(hash) || refillService.getRequestIdByMerchantIdAndCurrencyIdAndHash(merchant.getId(), currency.getId(), hash).isPresent();
    }
}
