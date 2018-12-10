package com.exrates.inout.service.apollo;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Log4j2(topic = "apollo")
@Service
public class ApolloNodeServiceImpl implements ApolloNodeService {

    @Value("${apollo.url}")
    private String url;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getTransactions(String address, long timestamp) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("requestType", "getBlockchainTransactions")
                .queryParam("nonPhasedOnly", true)
                .queryParam("type", 0)
                .queryParam("subtype", 0)
                .queryParam("withMessage", true)
                .queryParam("executed", true)
                .queryParam("timestamp", timestamp)
                .queryParam("account", address);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                String.class).getBody();
    }

    @Override
    public String getTransaction(String txHash) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("requestType", "getTransaction")
                .queryParam("fullHash", txHash)
                .queryParam("includePhasingResult", true);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                String.class).getBody();
    }
}
