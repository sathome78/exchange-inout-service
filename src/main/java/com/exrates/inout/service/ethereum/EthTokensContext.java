package com.exrates.inout.service.ethereum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maks on 24.01.2018.
 */
//@Log4j2
@Component
public class EthTokensContext {

   private static final Logger log = LogManager.getLogger(EthTokensContext.class);


    @Autowired
    Map<String, EthTokenService> merchantServiceMap;

    Map<Integer, EthTokenService> merchantMapByCurrencies = new HashMap<>();
    Map<String, Integer> contractAddressByCurrencies = new HashMap<>();

    @PostConstruct
    private void init() {
        merchantServiceMap.forEach((k, v) -> {
            fillContractAddressMap(v);
        });
    }

    private void fillContractAddressMap(EthTokenService v) {
        try {
            merchantMapByCurrencies.put(v.currencyId(), v);
            v.getContractAddress().forEach((address) -> contractAddressByCurrencies.put(address, v.currencyId()));
        } catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    public EthTokenService getByCurrencyId(int currencyId) {
        return merchantMapByCurrencies.get(currencyId);
    }

    public boolean isContract(String contract) {
        if (contractAddressByCurrencies.get(contract) == null) {
            return false;
        } else {
            return true;
        }
    }

    public EthTokenService getByContract(String contract) {
        return getByCurrencyId(contractAddressByCurrencies.get(contract));
    }

}
