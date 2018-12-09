package com.exrates.inout.service.waves;

import com.exrates.inout.domain.WavesPayment;
import com.exrates.inout.domain.dto.waves.WavesAddress;
import com.exrates.inout.domain.dto.waves.WavesTransaction;
import com.exrates.inout.exceptions.WavesRestException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2(topic = "waves_log")
@Service
@Scope("prototype")
public class WavesRestClientImpl implements WavesRestClient {

    private static final String API_KEY_HEADER_NAME = "api_key";
    private static final int MAX_TRANSACTION_QUERY_LIMIT = 50;

    private static final String NEW_ADDRESS_ENDPOINT = "/addresses";
    private static final String TRANSFER_COSTS_ENDPOINT = "/assets/transfer";
    private static final String ACCOUNT_TRANSACTIONS_ENDPOINT = "/transactions/address/{address}/limit/{limit}";
    private static final String TRANSACTION_BY_ID_ENDPOINT = "/transactions/info/{id}";

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    private String host;
    private String port;
    private String apiKey;

    @Override
    public void init(String host, String port, String apiKey) {
        this.host = host;
        this.port = port;
        this.apiKey = apiKey;
    }

    @Override
    public String generateNewAddress() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY_HEADER_NAME, apiKey);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        return restTemplate.postForObject(generateBaseUrl() + NEW_ADDRESS_ENDPOINT, entity, WavesAddress.class).getAddress();
    }

    @Override
    public Integer getCurrentBlockHeight() {
        Integer height = restTemplate.exchange(generateBaseUrl() + "/blocks/height", HttpMethod.GET,
                new HttpEntity<>(""), new ParameterizedTypeReference<Map<String, Integer>>() {
                }).getBody().get("height");
        if (height == null) {
            throw new WavesRestException("Cannot obtain block height");
        }
        return height;
    }

    @Override
    public String transferCosts(WavesPayment payment) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(API_KEY_HEADER_NAME, apiKey);
        HttpEntity<WavesPayment> entity = new HttpEntity<>(payment, headers);
        try {
            return restTemplate.postForObject(generateBaseUrl() + TRANSFER_COSTS_ENDPOINT, entity, WavesTransaction.class).getId();
        } catch (HttpClientErrorException e) {
            try {
                JsonNode error = objectMapper.readTree(e.getResponseBodyAsString()).get("error");
                if (error == null) {
                    throw new WavesRestException(e);
                }
                throw new WavesRestException(e, error.asInt());
            } catch (IOException e1) {
                throw new WavesRestException(e1);
            }
        }
    }

    @Override
    public List<WavesTransaction> getTransactionsForAddress(String address) {
        Map<String, Object> params = new HashMap<>();
        params.put("address", address);
        params.put("limit", MAX_TRANSACTION_QUERY_LIMIT);
        ResponseEntity<List<List<WavesTransaction>>> transactionsResult = restTemplate.exchange(generateBaseUrl() + ACCOUNT_TRANSACTIONS_ENDPOINT,
                HttpMethod.GET, new HttpEntity<>(""), new ParameterizedTypeReference<List<List<WavesTransaction>>>() {
                }, params);
        return transactionsResult.getBody().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public Optional<WavesTransaction> getTransactionById(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return Optional.of(restTemplate.getForObject(generateBaseUrl() + TRANSACTION_BY_ID_ENDPOINT, WavesTransaction.class, params));
        } catch (Exception e) {
            log.error(e);
            return Optional.empty();
        }
    }

    private String generateBaseUrl() {
        return String.join(":", host, port);
    }
}
