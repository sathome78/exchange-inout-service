package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.service.InputOutputService;
import com.exrates.inout.service.RefillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static com.exrates.inout.util.RequestUtils.extractUserId;
import static com.exrates.inout.util.RequestUtils.extractUserRole;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InputApiController {

    private final InputOutputService inputOutputService;
    private final RefillService refillService;

    @PostMapping("/prepareCreditsOperation")
    public Optional<CreditsOperation> prepareCreditsOperation(HttpServletRequest request, @RequestBody Payment payment){
        return inputOutputService.prepareCreditsOperation(payment, extractUserId(request), extractUserRole(request));
    }

    @GetMapping("/checkInputRequestsLimit")
    public boolean checkInputRequestsLimit(HttpServletRequest request, @RequestParam("currency_id") int currencyId){
        return refillService.checkInputRequestsLimit(currencyId, extractUserRole(request), extractUserId(request));
    }

    @GetMapping("/getAddressByMerchantIdAndCurrencyIdAndUserId")
    public Optional<String> getAddressByMerchantIdAndCurrencyIdAndUserId(HttpServletRequest request,
                                                                         @RequestParam("currency_id") int currencyId,
                                                                         @RequestParam("merchant_id") int merchant_id){
        return refillService.getAddressByMerchantIdAndCurrencyIdAndUserId(merchant_id, currencyId, extractUserId(request));
    }

    @PostMapping("/createRefillRequest")
    public Map<String, Object> createRefillRequest(@RequestBody RefillRequestCreateDto request){
        return refillService.createRefillRequest(request);
    }
}
