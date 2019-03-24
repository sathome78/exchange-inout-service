package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.main.Commission;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.exceptions.CheckDestinationTagException;
import com.exrates.inout.service.casinocoin.CasinoCoinService;
import com.exrates.inout.service.casinocoin.CasinoCoinServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WithdrawApiControllerTest extends InoutTestApplication {

    @Autowired
    private CasinoCoinService casinoCoinService;

    @Test
    public void checkDestinationTag() throws Exception {
        int merchantId = ((CasinoCoinServiceImpl) casinoCoinService).getMerchant().getId();
        mvc.perform(get("/api/merchant/checkDestinationTag")
            .param("merchant_id", String.valueOf(merchantId))
            .param("memo", "123")
            .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE()))
                .andExpect(status().isOk());

        String contentAsString = mvc.perform(get("/api/merchant/checkDestinationTag")
                .param("merchant_id", String.valueOf(merchantId))
                .param("memo", "aaa")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE()))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        CheckDestinationTagException ex = objectMapper.readValue(contentAsString, CheckDestinationTagException.class);
        assertEquals(casinoCoinService.additionalWithdrawFieldName(), ex.getFieldName());
    }

    @Test //TODO test case of false response
    public void checkOutputRequestsLimit() throws Exception {
        User testUser = new User(100500, "email");
        UserRole userRole = UserRole.USER;
        int currencyId = 525;

        String result = mvc.perform(get("/api/withdraw/checkOutputRequestsLimit" + "?merchant_id=" + currencyId)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", userRole)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assert Boolean.valueOf(result);
    }

    @Test
    public void createWithdrawalRequest() throws Exception {
        User user = registerNewUser();

        BigDecimal commissionAmount = new BigDecimal(0.2);
        BigDecimal amount = new BigDecimal(10);
        String destination = "myWallet";
        String destinationTag = "memo";

        Commission commission = new Commission();
        commission.setId(20);
        commission.setOperationType(OperationType.OUTPUT);
        commission.setValue(commissionAmount);
        commission.setDateOfChange(new Date());

        CreditsOperation creditsOperation = new CreditsOperation.Builder()
                .initialAmount(amount)
                .amount(amount)
                .commissionAmount(amount.subtract(commissionAmount))
                .commission(commission)
                .operationType(OperationType.OUTPUT)
                .user(user)
                .currency(aunitCurrency)
                .wallet(new Wallet(aunitCurrency.getId(), user, new BigDecimal(11)))
                .merchant(aunitMerchant)
                .merchantCommissionAmount(BigDecimal.ZERO)
                .destination(destination)
                .destinationTag(destinationTag)
                .transactionSourceType(TransactionSourceType.WITHDRAW)
                .recipient(null)
                .recipientWallet(null)
                .build();
        WithdrawRequestParamsDto withdrawReqParams = new WithdrawRequestParamsDto();
        withdrawReqParams.setSum(amount);
        withdrawReqParams.setMerchant(aunitMerchant.getId());
        withdrawReqParams.setCurrency(aunitMerchant.getId());
        withdrawReqParams.setDestination(destination);
        withdrawReqParams.setDestinationTag(destinationTag);
        withdrawReqParams.setOperationType(OperationType.OUTPUT);
        withdrawReqParams.setMerchantImage(228);

        Mockito.when(walletService.ifEnoughMoney(anyInt(), any())).thenReturn(true);
        Mockito.when(walletService.getWalletABalance(anyInt())).thenReturn(amount);
        Mockito.when(walletService.walletInnerTransfer(anyInt(), any(), eq(TransactionSourceType.WITHDRAW), anyInt(), anyString())).thenReturn(WalletTransferStatus.SUCCESS);

        WithdrawRequestCreateDto dto = new WithdrawRequestCreateDto(withdrawReqParams, creditsOperation, (WithdrawStatusEnum) WithdrawStatusEnum.getBeginState());

        String response = mvc.perform(post("/api/withdraw/request/create")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("locale", "en")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        Map<String, String> result = objectMapper.readValue(response, new TypeReference<Map<String, String>>() {{}});
        System.out.println(result);

    }
}