package com.exrates.inout.controller;

import com.exrates.inout.domain.enums.MerchantProcessType;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.service.*;
import com.exrates.inout.service.achain.AchainService;
import com.exrates.inout.service.aidos.AdkService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import com.exrates.inout.service.nem.NemService;
import com.exrates.inout.service.stellar.StellarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminApiController {

    private final MerchantServiceContext serviceContext;
    private final CurrencyService currencyService;
    private final MerchantService merchantService;
    private final EDCServiceNode edcServiceNode;
    private final AdkService adkService;
    private final AchainService achainService;
    private final NemService nemService;
    private final StellarService stellarService;

    @GetMapping(value = "/getWalletBalanceByCurrencyName")
    public ResponseEntity<Map<String, String>> getWalletBalanceByCurrencyName(@RequestParam("currency") String currencyName,
                                                                              @RequestParam("token") String token,
                                                                              @RequestParam(value = "address", required = false) String address) throws IOException {

        if (!token.equals("ZXzG8z13nApRXDzvOv7hU41kYHAJSLET")) {
            throw new RuntimeException("Some unexpected exception");
        }
        if (currencyName.equals("EDR")) {
            String balance = edcServiceNode.extractBalance(address, 0);
            Map<String, String> response = new HashMap<>();
            response.put("EDR", balance);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Currency byName = currencyService.findByName(currencyName);

        List<Merchant> allByCurrency = merchantService.findAllByCurrency(byName);
        List<Merchant> collect = allByCurrency
                .stream().
                        filter(merchant -> merchant.getProcessType() == MerchantProcessType.CRYPTO).collect(Collectors.toList());
        Map<String, String> collect1 = collect.
                stream().
                collect(Collectors.toMap(
                        Merchant::getName,
                        merchant -> getBitcoinServiceByMerchantName(merchant.getName()).getWalletInfo().getBalance()));


        return new ResponseEntity<>(collect1, HttpStatus.OK);
    }


    private BitcoinService getBitcoinServiceByMerchantName(String merchantName) {
        String serviceBeanName = merchantService.findByName(merchantName).getServiceBeanName();
        IMerchantService merchantService = serviceContext.getMerchantService(serviceBeanName);
        if (merchantService == null || !(merchantService instanceof BitcoinService)) {
            throw new RuntimeException("No request bean found exception " + serviceBeanName);
        }
        return (BitcoinService) merchantService;
    }

    @GetMapping("/adk/getBalance")
    public String adkGetBalance() {
        return adkService.getBalance();
    }

    @GetMapping("/countSpecCommission/{merchantName}")
    public BigDecimal countSpecCommission(@RequestParam("amount") BigDecimal amount,
                                          @RequestParam("destinationTag") String destinationTag,
                                          @RequestParam("merchantId") Integer merchantId,
                                          @PathVariable("merchantName") String merchantName){
        IMerchantService byMerchantName = serviceContext.getMerchantServiceByName(merchantName);
        return ((IWithdrawable) byMerchantName).countSpecCommission(amount, destinationTag, merchantId);
    }
}
