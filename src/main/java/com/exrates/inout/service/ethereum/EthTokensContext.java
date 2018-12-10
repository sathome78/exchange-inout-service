package com.exrates.inout.service.ethereum;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class EthTokensContext {

    @Autowired
    Map<String, EthTokenService> merchantServiceMap;

    private Map<Integer, EthTokenService> merchantMapByCurrencies = new HashMap<>();
    private Map<String, Integer> contractAddressByCurrencies = new HashMap<>();

    @PostConstruct
    private void init() {
        merchantServiceMap.forEach((k,v)-> {
            merchantMapByCurrencies.put(v.currencyId(), v);
            v.getContractAddress().forEach((address)->{
                contractAddressByCurrencies.put(address, v.currencyId());
            });
        });
    }

    EthTokenService getByCurrencyId(int currencyId) {
        return merchantMapByCurrencies.get(currencyId);
    }

    boolean isContract(String contract){
        return contractAddressByCurrencies.get(contract) != null;
    }

    EthTokenService getByContract(String contract){
        return getByCurrencyId(contractAddressByCurrencies.get(contract));
    }
}
