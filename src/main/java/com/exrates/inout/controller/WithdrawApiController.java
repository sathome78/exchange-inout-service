package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.dto.NormalizeAmountDto;
import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawableDataDto;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.service.IWithdrawable;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.WithdrawService;
import com.exrates.inout.service.aidos.AdkService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

import static com.exrates.inout.util.RequestUtils.*;

@RestController
@RequestMapping("/api/withdraw")
@RequiredArgsConstructor
public class WithdrawApiController {

    private final MerchantService merchantService;
    private final WithdrawService withdrawService;
    private final MerchantServiceContext serviceContext;

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

    @GetMapping("/checkOutputRequestsLimit")
    public boolean checkOutputRequestsLimit(HttpServletRequest request, @RequestParam("merchant_id") int merchantId){
        return withdrawService.checkOutputRequestsLimit(merchantId, extractUserId(request), extractUserRole(request));
    }

    @PostMapping("/request/create")
    public Map<String, String> createWithdrawalRequest(HttpServletRequest request, @RequestBody WithdrawRequestCreateDto dto){
        return withdrawService.createWithdrawalRequest(dto, extractUserLocale(request));
    }

    @GetMapping("/getAdditionalServiceData/merchantId")
    public WithdrawableDataDto getAdditionalServiceData(@PathVariable("merchantId") Integer merchantId){
        IWithdrawable withdrawable = (IWithdrawable) serviceContext.getMerchantService(merchantId);
        WithdrawableDataDto dto = new WithdrawableDataDto();
        dto.setAdditionalTagForWithdrawAddressIsUsed(withdrawable.additionalTagForWithdrawAddressIsUsed());
        dto.setAdditionalWithdrawFieldName(withdrawable.additionalWithdrawFieldName());
        return dto;
    }
}
