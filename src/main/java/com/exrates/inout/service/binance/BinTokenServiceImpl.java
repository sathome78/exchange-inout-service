package com.exrates.inout.service.binance;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.models.BinanceCoins;
import com.exrates.inout.properties.models.BinanceTokenProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.util.CryptoUtils;
import com.exrates.inout.util.WithdrawUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Log4j2(topic = "binance_log")
public class BinTokenServiceImpl implements BinTokenService {

    private String mainAddress;
    private String merchantName;
    private String currencyName;
    private Currency currency;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private WithdrawUtils withdrawUtils;

    public BinTokenServiceImpl(BinanceCoins binanceCoins, BinanceTokenProperty binanceTokenProperty){

        this.mainAddress = binanceCoins.getBinance().getBinanceMainAddress();
        this.merchantName = binanceTokenProperty.getMerchantName();
        this.currencyName = binanceTokenProperty.getCurrencyName();
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
