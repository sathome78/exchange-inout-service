package com.exrates.inout.service.adgroup;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.constants.ErrorApiTitles;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.adgroup.AdGroupCommonRequestDto;
import com.exrates.inout.domain.dto.adgroup.AdGroupFetchTxDto;
import com.exrates.inout.domain.dto.adgroup.CommonAdGroupHeaderDto;
import com.exrates.inout.domain.dto.adgroup.enums.TxStatus;
import com.exrates.inout.domain.dto.adgroup.responses.AdGroupResponseDto;
import com.exrates.inout.domain.dto.adgroup.responses.ResponseListTxDto;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.NgDashboardException;
import com.exrates.inout.service.MerchantService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@EnableScheduling
@Service
public class AdGroupSchedulerService {

    private static final Logger log = LogManager.getLogger("AdGroup");

    private final String clientId;
    private final String clientSecret;
    private final String url;
    private final String coreUrl;

    private final MerchantService merchantService;
    private final RefillRequestDao refillRequestDao;
    private final RestTemplate template;

    @Autowired
    public AdGroupSchedulerService(@Value("${payment-system-merchants.adgroup.client_id}") String clientId,
                                   @Value("${payment-system-merchants.adgroup.client_secret}") String clientSecret,
                                   @Value("${payment-system-merchants.adgroup.base_url}") String url,
                                   @Value("${core_url_back}") String coreUrl,
                                   MerchantService merchantService,
                                   RefillRequestDao refillRequestDao) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.url = url;
        this.coreUrl = coreUrl;
        this.merchantService = merchantService;
        this.refillRequestDao = refillRequestDao;

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(30000);
        this.template = new RestTemplate(requestFactory);
    }

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void regularlyCheckStatusTransactions() {
        log.info("*** Ad_Group starting check tx ***");
        Merchant merchantWallet = merchantService.findByName("Adgroup_Wallet");
        Merchant merchantPaymentCard = merchantService.findByName("Adgroup_PaymentCard");
        List<RefillRequestFlatDto> pendingTx = refillRequestDao.getByMerchantIdAndRemark(merchantWallet.getId(), "PENDING");
        List<RefillRequestFlatDto> pendingTxPaymentCard = refillRequestDao.getByMerchantIdAndRemark(merchantPaymentCard.getId(), "PENDING");
        pendingTx.addAll(pendingTxPaymentCard);

        if (pendingTx.isEmpty()) {
            log.info("*** Ad_Group stopped check tx, empty list ***");
            return;
        }
        log.info("Staring check transactions {}", pendingTx);
        final String requestUrl = url + "/transfer/get-merchant-tx";
        List<String> txStrings = pendingTx.stream().map(RefillRequestFlatDto::getMerchantTransactionId).collect(Collectors.toList());

        CommonAdGroupHeaderDto header = new CommonAdGroupHeaderDto("fetchMerchTx", 0.1);
        AdGroupFetchTxDto requestBody = AdGroupFetchTxDto.builder()
                .start(0)
                .limit(pendingTx.size())
                .txStatus(new String[]{"PENDING", "APPROVED", "REJECTED", "CREATED", "INVOICE"})
                .orderId(txStrings.toArray(new String[0]))
                .build();

        AdGroupCommonRequestDto requestDto = new AdGroupCommonRequestDto<>(header, requestBody);
        AdGroupResponseDto<ResponseListTxDto> responseDto =
                getTransactions(requestUrl, getAuthorizationKey(), requestDto);

        log.info("Response from adgroup {}", responseDto);
        for (RefillRequestFlatDto transaction : pendingTx) {
            responseDto.getResponseData().getTransactions()
                    .stream()
                    .filter(tx -> tx.getRefid().equalsIgnoreCase(transaction.getMerchantTransactionId()))
                    .peek(tx -> {
                        switch (TxStatus.valueOf(tx.getTxStatus())) {
                            case APPROVED:
                                Map<String, String> params = new HashMap<>();
                                params.put("amount", tx.getAmount().toString());
                                params.put("currency", tx.getCurrency());
                                params.put("paymentId", transaction.getMerchantTransactionId());
                                params.put("userId", String.valueOf(transaction.getUserId()));
                                params.put("merchantId", String.valueOf(transaction.getMerchantId()));
                                sendPaymentToCore(params);
                                break;
                            case INVOICE:
                            case PENDING:
                            case CREATED:
                                break;
                            case REJECTED:
                                refillRequestDao.setRemarkById(transaction.getId(), "REJECTED");
                                break;
                        }
                    });
        }
    }

    private String getAuthorizationKey() {
        String forEncode = clientId + ":" + clientSecret;
        return Base64.getEncoder().encodeToString(forEncode.getBytes());
    }

    public AdGroupResponseDto<ResponseListTxDto> getTransactions(String url, String authorizationKey, AdGroupCommonRequestDto requestDto) {
        log.info("getApprovedTransactions(), {}", toJson(requestDto));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Basic " + authorizationKey);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build(true).toUri();

        HttpEntity<?> request = new HttpEntity<>(requestDto, headers);
        ResponseEntity<AdGroupResponseDto<ResponseListTxDto>> responseEntity;
        try {
            responseEntity =
                    template.exchange(uri, HttpMethod.POST, request,
                            new ParameterizedTypeReference<AdGroupResponseDto<ResponseListTxDto>>() {
                            });
        } catch (Exception e) {
            log.error("Error http request while fetch list transactions {}", e);
            throw new NgDashboardException(ErrorApiTitles.AD_GROUP_ERROR_HTTP_CLIENT);
        }

        HttpStatus httpStatus = responseEntity.getStatusCode();

        if (!httpStatus.is2xxSuccessful()) {
            String errorString = "Error while creating invoice ";
            log.error(errorString + " {}", responseEntity);
            throw new NgDashboardException(ErrorApiTitles.AD_GROUP_HTTP_CLIENT_RESPONSE_NOT_200);
        }
        log.info("Response : {}", toJson(responseEntity.getBody()));
        return responseEntity.getBody();
    }

    private String toJson(Object input) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(input);
        } catch (JsonProcessingException e) {
            log.error("Error create json from object");
            return StringUtils.EMPTY;
        }
    }

    private void sendPaymentToCore(Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        String urlRequest = coreUrl + "/merchants/adgroup/payment";
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

        RetryPolicy retryPolicy = new RetryPolicy()
                .withBackoff(1, 10, TimeUnit.SECONDS)
                .withMaxRetries(2)
                .retryOn(NgDashboardException.class);

        Failsafe.with(retryPolicy).get(() -> {
            ResponseEntity<Void> response;
            try {
                response = template.postForEntity(urlRequest, request, Void.class);
            } catch (RestClientException e) {
                throw new NgDashboardException("Failed to upload! " + e);
            }
            if (response.getStatusCodeValue() != 200) {
                throw new NgDashboardException("Failed to upload! status=" + response.getStatusCodeValue());
            }
            return response.getBody();
        });
    }
}
