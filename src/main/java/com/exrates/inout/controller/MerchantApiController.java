package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/api/merchant", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class MerchantApiController {

    private final MerchantServiceContext serviceContext;
    private final RefillService refillService;
    private final MerchantService merchantService;

    @GetMapping(value = "/getAdditionalRefillFieldName/{merchantId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getAdditionalRefillFieldName(@PathVariable("merchantId") String merchantId) {
        IRefillable refillable = (IRefillable) (serviceContext.getMerchantService(merchantId));
        if (refillable.additionalFieldForRefillIsUsed()) {
            return refillable.additionalRefillFieldName();
        }
        return null;
    }

    @GetMapping(value = "/getMinConfirmationsRefill/{merchantId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Integer getMinConfirmationsRefill(@PathVariable("merchantId") String merchantId) {
        IRefillable merchant = (IRefillable) serviceContext
                .getMerchantService(merchantId);
        return merchant.minConfirmationsRefill();

    }

    @PostMapping(value = "/retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MerchantCurrency> retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(@RequestBody List<MerchantCurrency> merchantCurrencies, @RequestParam("userEmail") String userEmail){
        return refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(merchantCurrencies, userEmail);
    }

    @PostMapping(value = "/callRefillIRefillable", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> callRefillIRefillable(@RequestBody RefillRequestCreateDto request) {
        return refillService.callRefillIRefillable(request);
    }

    @GetMapping("/checkDestinationTag")
    public Object checkDestinationTag(HttpServletResponse response,
                                      @RequestParam("merchant_id") int merchant_id,
                                      @RequestParam("memo") String memo){
        try {
            merchantService.checkDestinationTag(merchant_id, memo);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CheckDestinationTagException e) {
            response.setStatus(400);
            return e;
        }
    }

    @GetMapping("/isValidDestinationAddress")
    public Object isValidDestinationAddress(
                                      @RequestParam("merchantId") int merchantId,
                                      @RequestParam("address") String address){
        return merchantService.isValidDestinationAddress(merchantId, address);
    }
}
