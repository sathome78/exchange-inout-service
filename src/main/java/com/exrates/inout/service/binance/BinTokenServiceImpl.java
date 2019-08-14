package com.exrates.inout.service.binance;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.util.CryptoUtils;
import com.exrates.inout.util.WithdrawUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log4j2(topic = "binance_log")
public class BinTokenServiceImpl implements BinTokenService {

    private String currencyName;
    private String merchantName;
    private String mainAddress;
    private Currency currency;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WithdrawUtils withdrawUtils;

    public BinTokenServiceImpl(String propertySource, String merchantName, String currencyName){
        Properties props = new Properties();

        try {
            props.load(getClass().getClassLoader().getResourceAsStream(propertySource));
            this.mainAddress = props.getProperty("binance.main.address");
        } catch (IOException e) {
            log.error(e);
        }

        this.merchantName = merchantName;
        this.currencyName = currencyName;
    }

    @PostConstruct
    public void init() {
        currency = currencyService.findByName(currencyName);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String destinationTag = CryptoUtils.generateDestinationTag(request.getUserId(),
                9 + request.getUserId().toString().length(), currency.getName());
        String message = messageSource.getMessage("merchants.refill.xlm",
                new Object[]{mainAddress, destinationTag}, request.getLocale());
        return new HashMap<String, String>() {{
            put("address", destinationTag);
            put("message", message);
            put("qr", mainAddress);
        }};
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {

    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        throw new RuntimeException("not supported");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(mainAddress, address);
    }

    @Override
    public String getMerchantName() {
        return merchantName;
    }

    @Override
    public String getCurrencyName() {
        return currencyName;
    }

    @Override
    public String getMainAddress(){
        return mainAddress;
    }
}
