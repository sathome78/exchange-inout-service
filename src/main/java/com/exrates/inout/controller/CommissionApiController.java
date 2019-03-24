package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.dto.NormalizeAmountDto;
import com.exrates.inout.service.CommissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/commission", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class CommissionApiController {

    private final CommissionService commissionService;

    @PostMapping("/normalizeAmountAndCalculateCommission")
    public CommissionDataDto normalizeAmountAndCalculateCommission(@RequestBody NormalizeAmountDto dto) {
        return commissionService.normalizeAmountAndCalculateCommission(dto.getUserId(), dto.getAmount(), dto.getType(), dto.getCurrencyId(), dto.getMerchantId(), dto.getDestinationTag(), dto.getUserRole());
    }
}
