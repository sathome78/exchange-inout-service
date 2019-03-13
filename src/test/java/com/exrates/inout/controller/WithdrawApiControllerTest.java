package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.service.casinocoin.CasinoCoinService;
import com.exrates.inout.service.casinocoin.CasinoCoinServiceImpl;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WithdrawApiControllerTest extends InoutTestApplication {

    @Autowired
    private CasinoCoinService casinoCoinService;

    @Test
    public void checkDestinationTag() throws Exception {
        int merchantId = ((CasinoCoinServiceImpl) casinoCoinService).getMerchant().getId();
        mvc.perform(get("/api/checkDestinationTag")
            .param("merchant_id", String.valueOf(merchantId))
            .param("memo", "123")
            .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE()))
                .andExpect(status().isOk());

        String contentAsString = mvc.perform(get("/api/checkDestinationTag")
                .param("merchant_id", String.valueOf(merchantId))
                .param("memo", "aaa")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE()))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        CheckDestinationTagException ex = objectMapper.readValue(contentAsString, CheckDestinationTagException.class);
        assertEquals(casinoCoinService.additionalWithdrawFieldName(), ex.getFieldName());
    }
}