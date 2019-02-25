package com.exrates.inout.service.impl;

import com.exrates.inout.InoutTestConfig;
import com.exrates.inout.controller.RefillRequestController;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.dto.UserInfoDto;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.btc.BitcoinServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

public class RefillServiceImplTest extends InoutTestConfig {


    @Autowired
    private RefillRequestController refillRequestController;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RefillService refillService;

    @MockBean(name = "bitcoinServiceImpl")
    private BitcoinServiceImpl bitcoinService;

    @Test
    public void createRefillRequest() {
        Merchant merchant = merchantService.findByName("BTC");
        Currency currency = currencyService.findByName("BTC");
        String address = "TestAddres132daws";

        Mockito.when(bitcoinService.refill(any())).thenReturn(new HashMap<String, String>() {{
            put("address", address);
            put("message", "Send bablo here");
            put("qr", "TestAddresdaws");
        }});

        Principal authentication = Mockito.mock(Principal.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        UserInfoDto userInfoDto = registerNewUser();
        Mockito.when(authentication.getName()).thenReturn(userInfoDto.getEmail());
        Mockito.when(request.getUserPrincipal()).thenReturn(authentication);
        Mockito.when(request.getAttribute(any())).thenReturn(new Locale("en"));

        RefillRequestParamsDto refillReqDto = new RefillRequestParamsDto();
        refillReqDto.setCurrency(currency.getId());
        refillReqDto.setMerchant(merchant.getId());
        refillReqDto.setSum(null);
        refillReqDto.setOperationType(OperationType.INPUT);
        refillReqDto.setGenerateNewAddress(true);

        Map<String, Object> result = refillRequestController.createRefillRequest(refillReqDto, authentication, new Locale("en"), request);

        Optional<RefillRequestFlatDto> refillRequestFlatDtoOptional = refillService.getByAddressAndMerchantIdAndCurrencyIdAndUserId(address, merchant.getId(), currency.getId(), userInfoDto.getId());
        assertTrue(refillRequestFlatDtoOptional.isPresent());

        RefillRequestFlatDto refillRequestFlatDto = refillRequestFlatDtoOptional.get();
        assertEquals(address, refillRequestFlatDto.getAddress());
        assertEquals(userInfoDto.getId(), refillRequestFlatDto.getUserId());
        assertEquals(currency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(merchant.getId(), (long) refillRequestFlatDto.getMerchantId());
    }

}