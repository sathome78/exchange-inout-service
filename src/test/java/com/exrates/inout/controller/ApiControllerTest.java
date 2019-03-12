package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.controller.interceptor.TokenInterceptor;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
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
import static org.junit.Assert.assertFalse;
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
    @Autowired
    private RefillRequestDao refillRequestDao;

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
        TestUser testUser = registerNewUser();
        String address = "test";

        String response = performGetAddressMyMerchantIdAndCurrencyIdAndUserId(testUser, aunitCurrency.getId(), aunitMerchant.getId());
        Optional<String> o = objectMapper.readValue(response, new TypeReference<Optional<String>>() {{
        }});
        assertFalse(o.isPresent());

        storeRefillRequest(testUser, address);

        response = performGetAddressMyMerchantIdAndCurrencyIdAndUserId(testUser, aunitCurrency.getId(), aunitMerchant.getId());
        assertEquals(address, response);
    }

    private void storeRefillRequest(TestUser testUser, String address) {
        RefillRequestCreateDto refillRequestCreateDto = new RefillRequestCreateDto();
        refillRequestCreateDto.setUserId(testUser.getId());
        refillRequestCreateDto.setMerchantId(aunitMerchant.getId());
        refillRequestCreateDto.setCurrencyId(aunitCurrency.getId());
        refillRequestCreateDto.setAmount(new BigDecimal(10));
        refillRequestCreateDto.setStatus(RefillStatusEnum.ACCEPTED_AUTO);
        refillRequestCreateDto.setCommissionId(6);
        refillRequestCreateDto.setNeedToCreateRefillRequestRecord(true);
        refillRequestCreateDto.setAddress(address);
        refillRequestDao.storeRefillRequestAddress(refillRequestCreateDto);
    }

    private String performGetAddressMyMerchantIdAndCurrencyIdAndUserId(TestUser testUser, int currencyId, int merchantId) throws Exception {
        return mvc.perform(get("/getAddressByMerchantIdAndCurrencyIdAndUserId"
                + "?currency_id=" + currencyId
                + "&merchant_id=" + merchantId)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", testUser.getRole())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }
}