package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.controller.interceptor.TokenInterceptor;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.dto.TestUser;
import com.exrates.inout.properties.SsmProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ApiControllerTest extends InoutTestApplication {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SsmProperties ssmProperties;
    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Test
    public void prepareCreditsOperation() throws Exception {

        double amountForRefill = 1;
        OperationType operationType = INPUT;
        Payment payment = new Payment(operationType);
        payment.setCurrency(aunitCurrency.getId());
        payment.setMerchant(aunitMerchant.getId());
        payment.setSum(amountForRefill);

        TestUser testUser = new TestUser(100500, "email");
        UserRole userRole = UserRole.USER;

        Wallet wallet = new Wallet(aunitCurrency.getId(), new User(testUser.getId()), null);
        Mockito.when(walletService.findByUserAndCurrency(anyInt(), anyInt())).thenReturn(wallet);

        String result = mvc.perform(post("/prepareCreditsOperation")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", userRole)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Optional<CreditsOperation> creditsOperationOptional = objectMapper.readValue(result, new TypeReference<Optional<CreditsOperation>>(){{}});
        assert creditsOperationOptional.isPresent();

        CreditsOperation creditsOperation = creditsOperationOptional.get();
        assertEquals((int)testUser.getId(), creditsOperation.getUser().getId());
        assertEquals(userRole, creditsOperation.getUser().getRole());
        assertEquals(wallet, creditsOperation.getWallet());
        assertEquals(aunitCurrency, creditsOperation.getCurrency());
        assertEquals(aunitMerchant, creditsOperation.getMerchant());
        assertEquals(new BigDecimal(amountForRefill), creditsOperation.getAmount());
        assertEquals(operationType, creditsOperation.getOperationType());
        assertEquals(TransactionSourceType.REFILL, creditsOperation.getTransactionSourceType());
    }


    @Test
    public void checkInputRequestsLimit() throws Exception {
        TestUser testUser = new TestUser(100500, "email");
        UserRole userRole = UserRole.USER;

        String result = mvc.perform(get("/checkInputRequestsLimit" + "?currency_id=" + aunitCurrency.getId())
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", userRole)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assert Boolean.valueOf(result);

    }

    @Test
    public void getAddressByMerchantIdAndCurrencyIdAndUserId() throws Exception {
        TestUser testUser = new TestUser(100500, "email");

        String response = mvc.perform(get("/getAddressByMerchantIdAndCurrencyIdAndUserId"
                + "?currency_id=" + aunitCurrency.getId()
                + "&merchant_id=" + aunitMerchant.getId())
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", testUser.getRole())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        System.out.println("result = " + objectMapper.readValue(response, new TypeReference<Optional<String>>(){{}}));
    }
}