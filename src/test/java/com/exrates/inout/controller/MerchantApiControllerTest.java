package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.service.omni.OmniService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MerchantApiControllerTest extends InoutTestApplication {

    @Autowired
    private OmniService omniService;

    @Test
    public void getMinConfirmationsRefillNullable() throws Exception {
        String component = UriComponentsBuilder.fromUriString("/api/merchant/getMinConfirmationsRefill/")
                .path(String.valueOf(aunitMerchant.getId())).build().encode().toUriString();;

        String content = mvc.perform(get(component)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assert content.equals(StringUtils.EMPTY);
    }

    @Test
    public void getMinConfirmationsRefill() throws Exception {
        String component = UriComponentsBuilder.fromUriString("/api/merchant/getMinConfirmationsRefill/")
                .path(String.valueOf(omniService.getMerchant().getId())).build().encode().toUriString();;

        String content = mvc.perform(get(component)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertEquals(omniService.minConfirmationsRefill(), Integer.valueOf(content));
    }

    @Test
    public void callRefillIRefillable() throws Exception {
        RefillRequestCreateDto refillRequestCreateDto = new RefillRequestCreateDto();
        refillRequestCreateDto.setUserId(123);
        refillRequestCreateDto.setMerchantId(aunitMerchant.getId());
        refillRequestCreateDto.setCurrencyId(aunitCurrency.getId());
        refillRequestCreateDto.setAmount(new BigDecimal(10));
        refillRequestCreateDto.setStatus(RefillStatusEnum.ACCEPTED_AUTO);
        refillRequestCreateDto.setCommissionId(6);
        refillRequestCreateDto.setNeedToCreateRefillRequestRecord(true);
        refillRequestCreateDto.setAddress("123");
        refillRequestCreateDto.setServiceBeanName("aunitServiceImpl");

        String response = mvc.perform(post("/api/merchant/callRefillIRefillable")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(refillRequestCreateDto))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map<String, String> map = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {{
        }});
        assertEquals(3, map.size());
    }
}