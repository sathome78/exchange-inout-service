package com.exrates.inout.service.impl;

import com.exrates.inout.InoutApplication;
import com.exrates.inout.controller.RefillRequestController;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.service.btc.BitcoinServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = InoutApplication.class)
@RunWith(SpringRunner.class)
public class RefillServiceImplTest {

    @Autowired
    private RefillRequestController refillRequestController;

    @MockBean(name = "bitcoinServiceImpl")
    private BitcoinServiceImpl bitcoinService;

    @Test
    public void createRefillRequest() throws Exception {

        Mockito.when(bitcoinService.refill(any())).thenReturn(new HashMap<String, String>() {{
            put("address", "TestAddress");
            put("message", "Send bablo here");
            put("qr", "TestAddress");
        }});
        Principal authentication = Mockito.mock(Principal.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(authentication.getName()).thenReturn("mikita.malykov@upholding.biz");
        Mockito.when(request.getUserPrincipal()).thenReturn(authentication);

        RefillRequestParamsDto refillReqDto = new RefillRequestParamsDto();
        refillReqDto.setCurrency(4);
        refillReqDto.setMerchant(3);
        refillReqDto.setSum(null);
        refillReqDto.setOperationType(OperationType.INPUT);
        refillReqDto.setGenerateNewAddress(true);

        Map<String, Object> result = refillRequestController.createRefillRequest(refillReqDto, authentication, new Locale("en"), request);
        System.out.println("Result: " + result);
    }
}