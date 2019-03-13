package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.domain.dto.TestUser;
import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawRequestParamsDto;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.service.casinocoin.CasinoCoinService;
import com.exrates.inout.service.casinocoin.CasinoCoinServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test //TODO test case of false response
    public void checkOutputRequestsLimit() throws Exception {
        TestUser testUser = new TestUser(100500, "email");
        UserRole userRole = UserRole.USER;
        int currencyId = 525;

        String result = mvc.perform(get("/api/checkOutputRequestsLimit" + "?merchant_id=" + currencyId)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", userRole)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assert Boolean.valueOf(result);
    }

    @Test
    public void createWithdrawalRequest() throws Exception {
        BigDecimal commissionAmount = BigDecimal.ZERO;
        BigDecimal amount = new BigDecimal(10);

        CreditsOperation creditsOperation = new CreditsOperation.Builder()
                .initialAmount(commissionAmount)
                .amount(amount)
                .commissionAmount(commissionAmount)
                .commission(commissionData.getCompanyCommission())
                .operationType(operationType)
                .user(user)
                .currency(currency)
                .wallet(wallet)
                .merchant(merchant)
                .merchantCommissionAmount(commissionData.getMerchantCommissionAmount())
                .destination(destination)
                .destinationTag(destinationTag)
                .transactionSourceType(transactionSourceType)
                .recipient(recipient)
                .recipientWallet(recipientWallet)
                .build();
        WithdrawRequestParamsDto withdrawReqParams = new WithdrawRequestParamsDto();
        WithdrawRequestCreateDto dto = new WithdrawRequestCreateDto(withdrawReqParams, creditsOperation, (WithdrawStatusEnum) WithdrawStatusEnum.getBeginState());

        String response = mvc.perform(post("/api/withdraw/request/create")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("locale", "en")
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map<String, String> result = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {{}});
        System.out.println(result);

    }
}