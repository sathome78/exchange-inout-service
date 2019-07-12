package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.CryptoCurrencyBalances;
import com.exrates.inout.service.MerchantService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Log4j2
@Service
public class CryptoCurrencyBalancesImpl implements CryptoCurrencyBalances {

   private static final Logger log = LogManager.getLogger(CryptoCurrencyBalancesImpl.class);


    @Autowired
    Map<String, BitcoinService> bitcoinServiceMap;

    @Autowired
    MerchantService merchantService;

    public Map<Integer, String> getBalances() {

        List<Merchant> merchants = merchantService.findAll();
        Map<Integer, String> mapBalances = new HashMap<>();
        bitcoinServiceMap.entrySet().parallelStream().forEach(entry -> {
            try {
                String balance = entry.getValue().getWalletInfo().getBalance();
                if (balance != null) {
                    mapBalances.put(merchants.stream().filter(m -> m.getServiceBeanName().equals(entry.getKey())).findFirst().get().getId()
                            , balance);
                }
            } catch (Exception e) {
                log.error(e);
            }
        });
        return mapBalances;
    }
}
