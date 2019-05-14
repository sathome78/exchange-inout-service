package com.exrates.inout.service.cointest;

import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CoinTester;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.InputOutputService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.WithdrawService;
import com.exrates.inout.service.job.invoice.RefillRequestJob;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Map;

public abstract class CoinTestBasic implements CoinTester {

    protected String testEmail = "yagi3773@gmail.com";
    protected int userId;
    protected int currencyId;
    protected int merchantId;
    protected String name;
    @Autowired
    protected UserService userService;
    @Autowired
    protected Map<String, IRefillable> refillableServiceMap;
    @Autowired
    protected MerchantService merchantService;
    @Autowired
    protected InputOutputService inputOutputService;
    @Autowired
    protected RefillService refillService;
    @Autowired
    protected CurrencyService currencyService;
    @Autowired
    protected WithdrawService withdrawService;
    @Autowired
    protected RefillRequestJob refillRequestJob;
    @Autowired
    protected CryptoCurrencyProperties ccp;
    protected StringBuilder stringBuilder;

    protected CoinTestBasic(String name, String email, StringBuilder stringBuilder){
        this.name = name;
        this.stringBuilder = stringBuilder;
        if(email != null) this.testEmail = email;
        stringBuilder.append("Init success for coin " + name).append("<br>");

    }

    @PostConstruct
    public void init(){
        merchantId = merchantService.findByName(name).getId();
        currencyId = currencyService.findByName(name).getId();
        userId = userService.getIdByEmail(testEmail);
    }

}
