package com.exrates.inout.service.cointest;

import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.CoinTester;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.ethereum.EthTokenService;
import com.exrates.inout.service.nem.NemService;
import com.exrates.inout.service.waves.WavesService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@Service
public class CoinDispatcher {

    private final Map<String, IRefillable> stringIRefillableMap;
    private final Map<String, EthTokenService> stringEthTokenServiceMap;

    private final ApplicationContext applicationContext;

    @Autowired
    public CoinDispatcher(Map<String, IRefillable> stringIRefillableMap, Map<String, EthTokenService> stringEthTokenServiceMap, ApplicationContext applicationContext) {
        this.stringIRefillableMap = stringIRefillableMap;
        this.stringEthTokenServiceMap = stringEthTokenServiceMap;
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    public CoinTester getCoinTester(String merchantName, StringBuilder logger, String email){
        Object service = getService(merchantName);
        CoinTester coinTestService = null;
        if(service instanceof BitcoinService){
            coinTestService = new BtcCoinTester(merchantName, email, logger);
        }
        if(service instanceof EthTokenService){
            coinTestService = new EthTokenTester(merchantName, email, logger);
        }
        if(service instanceof NemService){
            coinTestService = new NemTest(merchantName, email, logger);
        }
        if(service instanceof WavesService){
            coinTestService = new WavesTest(merchantName, email, logger);
        }
        applicationContext.getAutowireCapableBeanFactory().autowireBean(coinTestService);
        runPostConstruct(coinTestService);
        return coinTestService;
    }

    private void runPostConstruct(CoinTester coinTestService) throws IllegalAccessException, InvocationTargetException {
        for (Method method : coinTestService.getClass().getMethods()) {
            if(method.isAnnotationPresent(PostConstruct.class)){
                method.invoke(coinTestService);
            }
        }
    }

    public Object getService(String merchantName){
        for (Map.Entry<String, IRefillable> entry : stringIRefillableMap.entrySet()) {
            if(entry.getValue().getMerchantName().equals(merchantName)) return entry.getValue();
        }
        for (Map.Entry<String, EthTokenService> entry : stringEthTokenServiceMap.entrySet()) {
            if(entry.getValue().getMerchantName().equals(merchantName)) return entry.getValue();
        }
        return null;
    }

}