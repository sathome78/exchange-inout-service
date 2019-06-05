package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawableDataDto;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.service.IWithdrawable;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.WithdrawService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.exrates.inout.util.RequestUtils.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping(value = "/api/withdraw", consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class WithdrawApiController {

    private final MerchantService merchantService;
    private final WithdrawService withdrawService;
    private final MerchantServiceContext serviceContext;

    @GetMapping("/checkOutputRequestsLimit")
    public boolean checkOutputRequestsLimit(HttpServletRequest request, @RequestParam("merchant_id") int merchantId){
        return withdrawService.checkOutputRequestsLimit(merchantId, extractUserId(request), extractUserRole(request));
    }

    @PostMapping("/request/create")
    public Map<String, String> createWithdrawalRequest(HttpServletRequest request, @RequestBody WithdrawRequestCreateDto dto){
        return withdrawService.createWithdrawalRequest(dto, extractUserLocale(request));
    }

    @GetMapping(value = "/getAdditionalServiceData/{merchantId}")
    public WithdrawableDataDto getAdditionalServiceData(@PathVariable("merchantId") Integer merchantId){
        IWithdrawable withdrawable = (IWithdrawable) serviceContext.getMerchantService(merchantId);
        WithdrawableDataDto dto = new WithdrawableDataDto();
        dto.setAdditionalTagForWithdrawAddressIsUsed(withdrawable.additionalTagForWithdrawAddressIsUsed());
        dto.setAdditionalWithdrawFieldName(withdrawable.additionalWithdrawFieldName());
        return dto;
    }

    @PostMapping("/retrieveAddressAndAdditionalParamsForWithdrawForMerchantCurrencies")
    public List<MerchantCurrency> retrieveAddressAndAdditionalParamsForWithdrawForMerchantCurrencies(@RequestBody List<MerchantCurrency> merchantCurrencies){
        return withdrawService.retrieveAddressAndAdditionalParamsForWithdrawForMerchantCurrencies(merchantCurrencies);
    }
}
