package com.exrates.inout.service.impl;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.TestUser;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.bitshares.aunit.AunitServiceImpl;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;


public class RefillTest extends InoutTestApplication {

    @Autowired
    private RefillService refillService;

    @Autowired
    @Qualifier("aunitServiceImpl")
    private AunitServiceImpl aunitService;

    @Test
    public void fullRefillTest() throws RefillRequestAppropriateNotFoundException {
        TestUser testUser = registerNewUser();
        Integer walletId = 1;
        int commissionId = 15;
        String merchantDescription = "Aunit Coin";
        String serviceBeanName = "aunitServiceImpl";
        boolean generateNewAddress = true;
        String locale = "en";

        RefillRequestCreateDto request = new RefillRequestCreateDto();
        request.setUserId(testUser.getId());
        request.setUserEmail(testUser.getEmail());
        request.setUserWalletId(walletId);
        request.setCurrencyId(aunitCurrency.getId());
        request.setMerchantId(aunitMerchant.getId());
        request.setCurrencyName(aunitCurrency.getName());
        request.setCommission(BigDecimal.ZERO);
        request.setCommissionId(commissionId);
        request.setMerchantDescription(merchantDescription);
        request.setServiceBeanName(serviceBeanName);
        request.setStatus(RefillStatusEnum.CREATED_USER);
        request.setGenerateNewAddress(generateNewAddress);
        request.setLocale(new Locale(locale));


        Map<String, Object> refillRequest = refillService.createRefillRequest(request);
        String address = (String) ((Map) refillRequest.get("params")).get("address");
        assert address.length() > 0;

        Optional<RefillRequestAddressDto> refillRequestFlatDtoOptional = refillService.getByAddressAndMerchantIdAndCurrencyIdAndUserId(address, aunitMerchant.getId(), aunitCurrency.getId(), testUser.getId());
        assertTrue(refillRequestFlatDtoOptional.isPresent());


        RefillRequestAddressDto refillRequestFlatDto = refillRequestFlatDtoOptional.get();
        assertEquals(address, refillRequestFlatDto.getAddress());
        assertEquals(testUser.getId(), refillRequestFlatDto.getUserId());
        assertEquals(aunitCurrency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(aunitMerchant.getId(), (long) refillRequestFlatDto.getMerchantId());

        String amount = "0.1";
        String hash = "hash123";

        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount);

        aunitService.createRequest(hash, address, new BigDecimal(amount));

        aunitService.processPayment(map);

        Optional<RefillRequestFlatDto> flatOptional = refillService.findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(address, aunitMerchant.getId(), aunitCurrency.getId(), hash);
        assertTrue(flatOptional.isPresent());
        RefillRequestFlatDto flatDto = flatOptional.get();

        assertTrue(compareObjects(amount, flatDto.getAmount()));

        Mockito.verify(rabbitService, Mockito.times(1)).sendAcceptRefillEvent(any());
    }




}