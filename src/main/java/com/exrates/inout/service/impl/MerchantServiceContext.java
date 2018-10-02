package com.exrates.inout.service.impl;

import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.MerchantNotFoundException;
import com.exrates.inout.exceptions.MerchantServiceBeanNameNotDefinedException;
import com.exrates.inout.exceptions.MerchantServiceNotFoundException;
import com.exrates.inout.service.IMerchantService;
import com.exrates.inout.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

@Component
public class MerchantServiceContext {
    @Autowired
    Map<String, IMerchantService> merchantServiceMap;

    @Autowired
    MerchantService merchantService;

    public IMerchantService getMerchantService(String serviceBeanName) {
        if (StringUtils.isEmpty(serviceBeanName)) {
            throw new MerchantServiceBeanNameNotDefinedException("");
        }
        return Optional.ofNullable(merchantServiceMap.get(serviceBeanName))
                .orElseThrow(() -> new MerchantServiceNotFoundException(serviceBeanName));
    }

    public IMerchantService getMerchantService(Integer merchantId) {
        Merchant merchant = Optional.ofNullable(merchantService.findById(merchantId))
                .orElseThrow(() -> new MerchantNotFoundException(String.valueOf(merchantId)));
        return getMerchantService(merchant.getServiceBeanName());
    }
}
