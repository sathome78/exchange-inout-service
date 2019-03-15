package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.omni.OmniBalanceDto;
import com.exrates.inout.domain.dto.omni.OmniTxDto;
import com.exrates.inout.domain.main.RefillRequestAddressShortDto;
import com.exrates.inout.service.omni.OmniService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usdt")
@RequiredArgsConstructor
public class UsdtApiController {

    private final OmniService omniService;

    @GetMapping("/getUsdtBalances")
    public OmniBalanceDto getUsdtBalances(){
        return omniService.getUsdtBalances();
    }

    @GetMapping("/getBtcBalance")
    public BigDecimal getBtcBalance(){
        return omniService.getBtcBalance();
    }

    @GetMapping("/getUsdtTransactions")
    public List<OmniTxDto> getOmniTransactions() {
        return omniService.getAllTransactions();
    }

    @GetMapping("/getUsdtBlockedAddersses")
    public List<RefillRequestAddressShortDto> getOmniBlockedAddressses() {
        return omniService.getBlockedAddressesOmni();
    }

    @GetMapping("/createUsdtTransaction")
    public String getOmniCreateRefill(@RequestBody Map<String, String> params) {
        omniService.createRefillRequestAdmin(params);
        return "OK";
    }

    @GetMapping("/minConfirmationsRefill")
    public Integer getUsdtMinConfirmationsRefill() {
        return omniService.minConfirmationsRefill();
    }
}
