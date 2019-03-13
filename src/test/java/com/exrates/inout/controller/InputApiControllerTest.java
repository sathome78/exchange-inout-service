package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.TestUser;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.service.RefillService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Optional;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class InputApiControllerTest extends InoutTestApplication {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RefillRequestDao refillRequestDao;
    @Autowired
    private RefillService refillService;

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

        String result = mvc.perform(post("/api/prepareCreditsOperation")
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


    @Test //TODO test case of false response
    public void checkInputRequestsLimit() throws Exception {
        TestUser testUser = new TestUser(100500, "email");
        UserRole userRole = UserRole.USER;

        String result = mvc.perform(get("/api/checkInputRequestsLimit" + "?currency_id=" + aunitCurrency.getId())
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

        response = objectMapper.readValue(performGetAddressMyMerchantIdAndCurrencyIdAndUserId(testUser, aunitCurrency.getId(), aunitMerchant.getId()), String.class);
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
        return mvc.perform(get("/api/getAddressByMerchantIdAndCurrencyIdAndUserId"
                + "?currency_id=" + currencyId
                + "&merchant_id=" + merchantId)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", testUser.getRole())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createRefillRequest() throws Exception {
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

        String response = mvc.perform(post("/api/createRefillRequest")
                .content(objectMapper.writeValueAsString(request))
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", testUser.getRole())
                .header("Content-Type", APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        String address = new JSONObject(response).getJSONObject("params").getString("address");
        assert address.length() > 0;

        Optional<RefillRequestAddressDto> refillRequestFlatDtoOptional = refillService.getByAddressAndMerchantIdAndCurrencyIdAndUserId(address, aunitMerchant.getId(), aunitCurrency.getId(), testUser.getId());
        assertTrue(refillRequestFlatDtoOptional.isPresent());

        RefillRequestAddressDto refillRequestFlatDto = refillRequestFlatDtoOptional.get();
        assertEquals(address, refillRequestFlatDto.getAddress());
        assertEquals(testUser.getId(), refillRequestFlatDto.getUserId());
        assertEquals(aunitCurrency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(aunitMerchant.getId(), (long) refillRequestFlatDto.getMerchantId());

    }
}