package com.exrates.inout.service.impl;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.controller.RefillRequestController;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.dto.UserInfoDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.WalletService;
import com.exrates.inout.service.bitshares.aunit.AunitServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

public class RefillServiceImplTest extends InoutTestApplication {

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

    @Resource
    private WalletService walletService;

    private Merchant aunitMerchant;
    private Currency aunitCurrency;

    @Before
    public void setUp(){
        aunitMerchant = merchantService.findByName("AUNIT");
        aunitCurrency = currencyService.findByName("AUNIT");
    }
    @Test
    public void createRefillRequestAddress() throws RefillRequestAppropriateNotFoundException {
        Principal principal = Mockito.mock(Principal.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        UserInfoDto userInfoDto = registerNewUser();
        Mockito.when(principal.getName()).thenReturn(userInfoDto.getEmail());
        Mockito.when(request.getUserPrincipal()).thenReturn(principal);
        Mockito.when(request.getAttribute(any())).thenReturn(new Locale("en"));
        Mockito.when(walletService.findByUserAndCurrency(any(), any())).thenReturn(new Wallet(-1, null, null));
        Mockito.when(walletService.walletBalanceChange(any())).thenReturn(WalletTransferStatus.SUCCESS);

        RefillRequestParamsDto refillReqDto = new RefillRequestParamsDto();
        refillReqDto.setCurrency(aunitCurrency.getId());
        refillReqDto.setMerchant(aunitMerchant.getId());
        refillReqDto.setSum(null);
        refillReqDto.setOperationType(OperationType.INPUT);
        refillReqDto.setGenerateNewAddress(true);

        Map<String, Object> result = refillRequestController.createRefillRequest(refillReqDto, principal, new Locale("en"), request);
        String address = (String) ((Map)result.get("params")).get("address");

        Optional<RefillRequestAddressDto> refillRequestFlatDtoOptional = refillService.getByAddressAndMerchantIdAndCurrencyIdAndUserId(address, aunitMerchant.getId(), aunitCurrency.getId(), userInfoDto.getId());
        assertTrue(refillRequestFlatDtoOptional.isPresent());

        RefillRequestAddressDto refillRequestFlatDto = refillRequestFlatDtoOptional.get();
        assertEquals(address, refillRequestFlatDto.getAddress());
        assertEquals(userInfoDto.getId(), refillRequestFlatDto.getUserId());
        assertEquals(aunitCurrency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(aunitMerchant.getId(), (long) refillRequestFlatDto.getMerchantId());

        String amount = "0.1";
        String hash = "hash123";
        Map<String, String> map = new HashMap<>();

        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount);
        aunitService.createRequest(hash, address, new BigDecimal(amount));

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(userInfoDto.getEmail());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        aunitService.processPayment(map);

        Optional<RefillRequestFlatDto> flatOptional = refillService.findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(address, aunitMerchant.getId(), aunitCurrency.getId(), hash);
        assertTrue(flatOptional.isPresent());
        RefillRequestFlatDto flatDto = flatOptional.get();

        assertTrue(compareObjects(amount, flatDto.getAmount()));
    }



    public static boolean compareObjects(Object A, Object B) {
        return normalize(A).equals(normalize(B));
    }

    private static String normalize(Object B) {
        BigDecimal A = new BigDecimal(String.valueOf(B));
        StringBuilder first = new StringBuilder(String.valueOf(A));
        String check = String.valueOf(A);
        if (!check.contains(".")) return check;

        String substring = "";
        substring = check.substring(check.indexOf(".") + 1);

        if (substring.length() > 8) {
            first = new StringBuilder(substring.substring(0, 8));
        } else first = new StringBuilder(substring.substring(0, substring.length()));


        for (int i = first.length() - 1; i != -1; i--) {
            if (String.valueOf(first.charAt(i)).equals("0")) {
                first.deleteCharAt(i);
            } else break;
        }
        String result = check.substring(0, check.indexOf(".") + 1) + first.toString();
        return result;
    }

}