package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
public class MerchantApiController {

    private final MerchantServiceContext serviceContext;
    private final RefillService refillService;

    @GetMapping("/getAdditionalRefillFieldName/{merchantId}")
    public String getAdditionalRefillFieldName(@PathVariable("merchantId") String merchantId) {
        IRefillable refillable = (IRefillable) (serviceContext.getMerchantService(merchantId));
        if (refillable.additionalFieldForRefillIsUsed()) {
            return refillable.additionalRefillFieldName();
        }
        return null;
    }

    @GetMapping("/getMinConfirmationsRefill/{merchantId}")
    public Integer getMinConfirmationsRefill(@PathVariable("merchantId") String merchantId) {
        IRefillable merchant = (IRefillable) serviceContext
                .getMerchantService(merchantId);
        return merchant.minConfirmationsRefill();

    }

    @PostMapping("/retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies")
    public List<MerchantCurrency> retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(@RequestBody List<MerchantCurrency> merchantCurrencies, @RequestParam("userEmail") String userEmail){
        return refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(merchantCurrencies, userEmail);
    }

    @PostMapping("/callRefillIRefillable")
    public Map<String, String> callRefillIRefillable(@RequestBody RefillRequestCreateDto request) {
        return refillService.callRefillIRefillable(request);
    }
}
