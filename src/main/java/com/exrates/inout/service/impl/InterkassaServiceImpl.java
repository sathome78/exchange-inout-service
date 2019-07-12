package com.exrates.inout.service.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.InterkassaProperty;
import com.exrates.inout.service.*;
import com.exrates.inout.util.WithdrawUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static java.util.Objects.isNull;

@Service
public class InterkassaServiceImpl implements InterkassaService {

    private static final Logger logger = LogManager.getLogger(InterkassaServiceImpl.class);

    private static final String POST = "post";

    private String url;
    private String checkoutId;
    private String statusUrl;
    private String successUrl;
    private String secretKey;
    private String interkassaSecretUrl;
    private String interkassaUsername;
    private String interkassaPassword;

    private AlgorithmService algorithmService;
    private RefillService refillService;
    private MerchantService merchantService;
    private CurrencyService currencyService;
    private RefillRequestDao refillRequestDao;
    private WithdrawUtils withdrawUtils;
    private GtagService gtagService;

    @Autowired
    public InterkassaServiceImpl(AlgorithmService algorithmService, RefillService refillService,
                                 MerchantService merchantService, CurrencyService currencyService,
                                 RefillRequestDao refillRequestDao, WithdrawUtils withdrawUtils,
                                 GtagService gtagService, CryptoCurrencyProperties cryptoCurrencyProperties){
        this.algorithmService = algorithmService;
        this.refillService = refillService;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.refillRequestDao = refillRequestDao;
        this.withdrawUtils = withdrawUtils;
        this.gtagService = gtagService;

        InterkassaProperty interkassaProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getInterkassa();
        this.url = interkassaProperty.getUrl();
        this.checkoutId = interkassaProperty.getCheckoutId();
        this.statusUrl = interkassaProperty.getStatusUrl();
        this.successUrl = interkassaProperty.getSuccessUrl();
        this.secretKey = interkassaProperty.getSecretKey();
        this.interkassaSecretUrl = interkassaProperty.getSecretUrl();
        this.interkassaUsername = interkassaProperty.getUsername();
        this.interkassaPassword = interkassaProperty.getPassword();
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer requestId = request.getId();
        if (isNull(requestId)) {
            throw new RefillRequestIdNeededException(request.toString());
        }
        final String currency = request.getCurrencyName();
        final BigDecimal amountToPay = request.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);

        final String interkassaId = getInterkassaMerchantId(request);

        Map<String, String> map = new TreeMap<>();
        map.put("ik_am", String.valueOf(amountToPay));
        map.put("ik_co_id", checkoutId);
        map.put("ik_cur", currency);
        map.put("ik_desc", "Exrates input");
        map.put("ik_ia_m", POST);
        map.put("ik_ia_u", statusUrl);
        map.put("ik_pm_no", String.valueOf(requestId));
        map.put("ik_pnd_m", POST);
        map.put("ik_pnd_u", statusUrl);
        map.put("ik_suc_u", successUrl);
        map.put("ik_suc_m", POST);
        map.put("ik_int", "json");
        map.put("ik_act", "process");
        map.put("ik_pw_via", interkassaId);

        map.put("ik_sign", getSignature(map));

        final InterkassaActionUrlDto actionUrlDto = getActionUrlDto(map);

        Properties properties = new Properties();
        properties.putAll(actionUrlDto.getParameters());

        return generateFullUrlMap(actionUrlDto.getActionURL(), actionUrlDto.getMethod(), properties);
    }

    private String getInterkassaMerchantId(RefillRequestCreateDto request) {
        final String childMerchant = request.getChildMerchant();
        final String currencyName = request.getCurrencyName();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(interkassaUsername, interkassaPassword));

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(interkassaSecretUrl + checkoutId, String.class);
        if (responseEntity.getStatusCodeValue() != 200) {
            throw new InterKassaMerchantException(
                    String.format("Attention! Problem with interkassa: %s (%d)",
                            responseEntity.getStatusCode().getReasonPhrase(),
                            responseEntity.getStatusCodeValue()));
        }
        JSONObject dataObject = new JSONObject(responseEntity.getBody())
                .getJSONObject("data");

        Iterator<String> keys = dataObject.keys();

        while (keys.hasNext()) {
            final String interkassaMerchantId = keys.next();

            JSONObject interkassaMerchantObject = dataObject.getJSONObject(interkassaMerchantId);

            if (interkassaMerchantObject.getString("ser").equalsIgnoreCase(childMerchant)
                    && interkassaMerchantObject.getString("curAls").equalsIgnoreCase(currencyName)) {
                return interkassaMerchantId;
            }
        }
        throw new InterKassaMerchantNotFoundException(
                String.format("Attention! Currency %s is not available for merchant %s",
                        currencyName,
                        childMerchant));
    }

    private InterkassaActionUrlDto getActionUrlDto(Map<String, String> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> headersMap = new LinkedMultiValueMap<>();
        map.forEach(headersMap::add);

        HttpEntity<MultiValueMap<String, String>> requestBody = new HttpEntity<>(headersMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestBody, String.class);
        if (responseEntity.getStatusCodeValue() != 200) {
            throw new InterKassaMerchantException(
                    String.format("Attention! Problem with interkassa: %s (%d)",
                            responseEntity.getStatusCode().getReasonPhrase(),
                            responseEntity.getStatusCodeValue()));
        }
        JSONObject paymentFormObject = new JSONObject(responseEntity.getBody())
                .getJSONObject("resultData")
                .getJSONObject("paymentForm");
        String actionUrl = paymentFormObject.getString("action").replace("\\", "");
        String method = paymentFormObject.getString("method").toUpperCase();
        JSONObject parametersObject = paymentFormObject.getJSONObject("parameters");

        Iterator<String> keys = parametersObject.keys();

        Map<String, Object> parameters = new HashMap<>();
        while (keys.hasNext()) {
            final String key = keys.next();

            Object value = parametersObject.get(key);

            parameters.put(key, value);
        }
        return InterkassaActionUrlDto.builder()
                .actionURL(actionUrl)
                .method(method)
                .parameters(parameters)
                .build();
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = Integer.valueOf(params.get("ik_pm_no"));
        String merchantTransactionId = params.get("ik_trn_id");
        Currency currency = currencyService.findByName(params.get("ik_cur"));
        Merchant merchant = merchantService.findByName("Interkassa");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("ik_am"))).setScale(9);
        String signature = params.get("ik_sign");

        params.remove("ik_sign");

        TreeMap<String, String> sortedParams = new TreeMap<>(params);

        String checkSignature = getSignature(sortedParams);

        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        if (checkSignature.equals(signature)
                && params.get("ik_co_id").equals(checkoutId)
                && params.get("ik_inv_st").equals("success")
                && refillRequest.getAmount().equals(amount)) {
            RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                    .requestId(requestId)
                    .merchantId(merchant.getId())
                    .currencyId(currency.getId())
                    .amount(amount)
                    .merchantTransactionId(merchantTransactionId)
                    .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                    .build();

            refillService.autoAcceptRefillRequest(requestAcceptDto);

            final String username = refillService.getUsernameByRequestId(requestId);

            logger.debug("Process of sending data to Google Analytics...");
            gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
        }
    }

    private String getSignature(final Map<String, String> params) {
        ArrayList<String> listValues = new ArrayList<>(params.values());

        listValues.add(secretKey);
        String stringValues = StringUtils.join(listValues, ":");

        byte[] signMD5 = algorithmService.computeMD5Byte(stringValues);

        return Base64.getEncoder().encodeToString(signMD5);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }
}