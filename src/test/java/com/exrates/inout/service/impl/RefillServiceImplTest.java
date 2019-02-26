package com.exrates.inout.service.impl;

import com.exrates.inout.InoutTestConfig;
import com.exrates.inout.controller.RefillRequestController;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.dto.UserInfoDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.bitshares.aunit.AunitServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    @Autowired
    private RefillRequestDao refillRequestDao;

    @Autowired
    @Qualifier("aunitServiceImpl")
    private AunitServiceImpl aunitService;

    private Merchant aunitMerchant;
    private Currency aunitCurrency;

    @Before
    public void setUp(){
        aunitMerchant = merchantService.findByName("AUNIT");
        aunitCurrency = currencyService.findByName("AUNIT");
    }
    @Test
    public void createRefillRequestAddress() throws RefillRequestAppropriateNotFoundException {
        Principal authentication = Mockito.mock(Principal.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        UserInfoDto userInfoDto = registerNewUser();
        Mockito.when(authentication.getName()).thenReturn(userInfoDto.getEmail());
        Mockito.when(request.getUserPrincipal()).thenReturn(authentication);
        Mockito.when(request.getAttribute(any())).thenReturn(new Locale("en"));

        RefillRequestParamsDto refillReqDto = new RefillRequestParamsDto();
        refillReqDto.setCurrency(aunitCurrency.getId());
        refillReqDto.setMerchant(aunitMerchant.getId());
        refillReqDto.setSum(null);
        refillReqDto.setOperationType(OperationType.INPUT);
        refillReqDto.setGenerateNewAddress(true);

        Map<String, Object> result = refillRequestController.createRefillRequest(refillReqDto, authentication, new Locale("en"), request);
        String address = (String) ((Map)result.get("params")).get("address");

        Optional<RefillRequestAddressDto> refillRequestFlatDtoOptional = refillService.getByAddressAndMerchantIdAndCurrencyIdAndUserId(address, aunitMerchant.getId(), aunitCurrency.getId(), userInfoDto.getId());
        assertTrue(refillRequestFlatDtoOptional.isPresent());

        RefillRequestAddressDto refillRequestFlatDto = refillRequestFlatDtoOptional.get();
        assertEquals(address, refillRequestFlatDto.getAddress());
        assertEquals(userInfoDto.getId(), refillRequestFlatDto.getUserId());
        assertEquals(aunitCurrency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(aunitMerchant.getId(), (long) refillRequestFlatDto.getMerchantId());

        //refill todo create wallet on startup
        String amount = "0.1";
        String hash = "hash123";
        Map<String, String> map = new HashMap<>();

        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount);
        aunitService.createRequest(hash, address, new BigDecimal(amount));
        aunitService.processPayment(map);


    }

}