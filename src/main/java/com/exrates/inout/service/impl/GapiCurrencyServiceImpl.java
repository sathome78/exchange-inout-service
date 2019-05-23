package com.exrates.inout.service.impl;

import com.exrates.inout.properties.models.GapiProperty;
import com.exrates.inout.properties.models.Gapicoin;
import com.exrates.inout.service.GapiCurrencyService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@Log4j2(topic = "gapi_log")
public class GapiCurrencyServiceImpl implements GapiCurrencyService {

    private RestTemplate restTemplate;

    private String mainaddress;

    private String serverIp;

    @Autowired
    public GapiCurrencyServiceImpl (GapiProperty gapiProperty){
        restTemplate = new RestTemplate();
        mainaddress = gapiProperty.getMainAddress();
        serverIp = gapiProperty.getServerIp();
    }

    public List<String> generateNewAddress() {
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("http://" + serverIp + "/api/v1/createnewwallet")
                .build();
        ResponseEntity<Wallet> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), Wallet.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.error("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.error("Error : {}", ex.getMessage());
        }

        String address = responseEntity.getBody().wallet.address;
        String privateKey = responseEntity.getBody().wallet.private_key;
        return Arrays.asList(address, privateKey);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Wallet {

        @JsonProperty("wallet")
        private Address wallet;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Address {

        @JsonProperty("wallet_id")
        private String address;
        @JsonProperty("private_key")
        private String private_key;
    }

    public List<Transaction> getAccountTransactions(){
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("http://" + serverIp + "/api/v1/alltransactions")
                .build();
        ResponseEntity<Transactions> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), Transactions.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.error("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.error("Error : {}", ex.getMessage());
        }
        Transaction[] transactions = responseEntity.getBody().transactions;
        return Arrays.asList(transactions);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Transactions {

        @JsonProperty("alltestsarecomplated")
        private Transaction[] transactions;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @Data
    public static class Transaction {

        @JsonProperty("senderwallet")
        private String senderAddress;
        @JsonProperty("receiver")
        private String recieverAddress;
        @JsonProperty("amount")
        private String amount;
        @JsonProperty("blockhash")
        private String transaction_id;
    }

    public String createNewTransaction(String privKey, String amount){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();

        map.add("sprikey", privKey);
        map.add("receiverwallet", mainaddress);
        map.add("amount", amount);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Response> response = restTemplate.postForEntity(
                "http://" + serverIp + "/api/v1/sendgapicoin", request , Response.class);

        return response.getBody().response;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Response {

        @JsonProperty("response")
        String response;
    }

}
