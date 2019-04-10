package com.exrates.inout.controller;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
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
import lombok.Data;
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
import static org.junit.Assert.*;
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
        PrepareCreditsOperationTestDto dto = prepareDto();
        assertResultIsValid(dto, execute(dto));
    }

    private PrepareCreditsOperationTestDto prepareDto() {
        PrepareCreditsOperationTestDto dto = new PrepareCreditsOperationTestDto();
        dto.setAmountForRefill(1);
        dto.setOperationType(INPUT);
        dto.setUserRole(UserRole.USER);
        dto.setTestUser(new User(100500, "email"));
        dto.setWallet(prepareWallet(dto.getTestUser()));
        return dto;
    }

    private Wallet prepareWallet(User testUser) {
        Wallet wallet = new Wallet(aunitCurrency.getId(), new User(testUser.getId()), null);
        Mockito.when(walletService.findByUserAndCurrency(anyInt(), anyInt())).thenReturn(wallet);
        return wallet;
    }

    private void assertResultIsValid(PrepareCreditsOperationTestDto dto, CreditsOperation creditsOperation) {
        assertNotNull(creditsOperation);

        assertEquals(dto.getTestUser().getId(), creditsOperation.getUser().getId());
        assertEquals(dto.getUserRole(), creditsOperation.getUser().getRole());
        assertEquals(dto.getWallet(), creditsOperation.getWallet());
        assertEquals(aunitCurrency, creditsOperation.getCurrency());
        assertEquals(aunitMerchant, creditsOperation.getMerchant());
        assertEquals(new BigDecimal(dto.getAmountForRefill()), creditsOperation.getAmount());
        assertEquals(dto.getOperationType(), creditsOperation.getOperationType());
        assertEquals(TransactionSourceType.REFILL, creditsOperation.getTransactionSourceType());
    }

    private CreditsOperation execute(PrepareCreditsOperationTestDto dto) throws Exception {
        Payment payment = preparePayment(dto.getAmountForRefill(), dto.getOperationType());


        String result = mvc.perform(post("/api/prepareCreditsOperation")
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", dto.getTestUser().getId())
                .header("user_role", dto.getUserRole())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payment))).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(result, CreditsOperation.class);
    }

    private Payment preparePayment(double amountForRefill, OperationType operationType) {
        Payment payment = new Payment(operationType);
        payment.setCurrency(aunitCurrency.getId());
        payment.setMerchant(aunitMerchant.getId());
        payment.setSum(amountForRefill);
        return payment;
    }

    @Test //TODO test case of false response
    public void checkInputRequestsLimit() throws Exception {
        User testUser = new User(100500, "email");
        UserRole userRole = UserRole.USER;

        String result = mvc.perform(get("/api/checkInputRequestsLimit" + "?currency_id=" + aunitCurrency.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", userRole)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        assert Boolean.valueOf(result);

    }

    @Test
    public void getAddressByMerchantIdAndCurrencyIdAndUserId() throws Exception {
        User testUser = registerNewUser();
        String address = "test";

        String response = performGetAddressMyMerchantIdAndCurrencyIdAndUserId(testUser, aunitCurrency.getId(), aunitMerchant.getId());
        Optional<String> o = objectMapper.readValue(response, new TypeReference<Optional<String>>() {{
        }});
        assertFalse(o.isPresent());

        storeRefillRequest(testUser, address);

        response = objectMapper.readValue(performGetAddressMyMerchantIdAndCurrencyIdAndUserId(testUser, aunitCurrency.getId(), aunitMerchant.getId()), String.class);
        assertEquals(address, response);
    }

    private void storeRefillRequest(User testUser, String address) {
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

    private String performGetAddressMyMerchantIdAndCurrencyIdAndUserId(User testUser, int currencyId, int merchantId) throws Exception {
        return mvc.perform(get("/api/getAddressByMerchantIdAndCurrencyIdAndUserId")
                .param("currency_id", String.valueOf(currencyId))
                .param("merchant_id", String.valueOf(merchantId))
                .header("Content-Type", APPLICATION_JSON)
                .header(tokenInterceptor.getAUTH_TOKEN_NAME(), tokenInterceptor.getAUTH_TOKEN_VALUE())
                .header("user_id", testUser.getId())
                .header("user_role", testUser.getRole())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void createRefillRequest() throws Exception {
        User testUser = registerNewUser();
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
        assertEquals(testUser.getId(), (int)refillRequestFlatDto.getUserId());
        assertEquals(aunitCurrency.getId(), (long) refillRequestFlatDto.getCurrencyId());
        assertEquals(aunitMerchant.getId(), (long) refillRequestFlatDto.getMerchantId());

    }

    @Data
    private class PrepareCreditsOperationTestDto {
        private double amountForRefill;
        private OperationType operationType;
        private UserRole userRole;
        private User testUser;
        private Wallet wallet;
    }

}