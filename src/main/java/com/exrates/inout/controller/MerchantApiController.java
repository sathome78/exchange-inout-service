package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    private final MerchantService merchantService;
    private final AdvcashService advcashService;
    private final EDCService edcService;
    private final InterkassaService interkassaService;
    private final NixMoneyService nixmoney;
    private final OkPayService okPayService;
    private final PayeerService payeerService;
    private final PerfectMoneyService perfectMoneyService;
    private final Privat24Service privat24Service;

    @GetMapping(value = "/getAdditionalRefillFieldName/{merchantId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAdditionalRefillFieldName(@PathVariable("merchantId") int merchantId) {
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
