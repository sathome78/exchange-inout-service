package com.exrates.inout.service.aisi;

import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.AisiProperty;
import com.exrates.inout.service.AlgorithmService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Log4j2(topic = "aisi_log")
@Service
public class AisiCurrencyServiceImpl implements AisiCurrencyService {

    private static final String AISI_CODE = "aisi_hash\":\"";

    private RestTemplate restTemplate;

    private AisiProperty aisiProperty;
    private AlgorithmService algorithmService;

    @Autowired
    public AisiCurrencyServiceImpl(CryptoCurrencyProperties ccp, AlgorithmService algorithmService) {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        restTemplate = new RestTemplate(requestFactory);

        this.algorithmService = algorithmService;
        this.aisiProperty = ccp.getAisiCoins().getAisi();
    }

    public String generateNewAddress() {
        final MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("api_key", "970E22216DA4C486CC22EEF9A58CD30E5B3A8A0D22A62F5D5B57222D16337814CEF3E7B1D7227C4754C733FE39F433F5C4E4E0F8B6D9D8F76F893BBA4");
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("https://api.aisi.io/account/address/new")
                .queryParams(requestParameters)
                .build();
        ResponseEntity<Address> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), Address.class);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.warn("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.warn("Error : {}", ex.getMessage());
        }
        return responseEntity.getBody().address;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Address {

        @JsonProperty("Address")
        String address;

    }

    public List<Transaction> getAccountTransactions(){
        Integer max = Integer.MAX_VALUE;
        final MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("api_key", "970E22216DA4C486CC22EEF9A58CD30E5B3A8A0D22A62F5D5B57222D16337814CEF3E7B1D7227C4754C733FE39F433F5C4E4E0F8B6D9D8F76F893BBA4");
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("https://api.aisi.io/transaction/account/{max}")
                .queryParams(requestParameters)
                .build();
        ResponseEntity<Transactions> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), Transactions.class, max);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.warn("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.warn("Error : {}", ex.getMessage());
        }

        Transaction[] transactions = responseEntity.getBody().transaction;
        return Arrays.asList(transactions);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Transactions {

        @JsonProperty("Transactions")
        Transaction[] transaction;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @Data
    public static class Transaction {

        @JsonProperty("Transaction_ID")
        private String transaction_id;
        @JsonProperty("TimeStamp")
        private String timeStamp;
        @JsonProperty("SenderAddress")
        private String senderAddress;
        @JsonProperty("RecieverAddress")
        private String recieverAddress;
        @JsonProperty("Amount")
        private String amount;
    }

    public String createNewTransaction(String address, BigDecimal amount){
        final MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("api_key", "970E22216DA4C486CC22EEF9A58CD30E5B3A8A0D22A62F5D5B57222D16337814CEF3E7B1D7227C4754C733FE39F433F5C4E4E0F8B6D9D8F76F893BBA4");
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("https://api.aisi.io/transaction/create/{FROM_ADDRESS}/{TO_ADDRESS}/{AMOUNT}")
                .queryParams(requestParameters)
                .build();
        ResponseEntity<CreatedTransaction> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), CreatedTransaction.class,
                    address, aisiProperty.getAisiMainaddress(), amount.toString());
            if (responseEntity.getStatusCodeValue() != 200) {
                log.warn("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.warn("Error : {}", ex.getMessage());
        }
        return responseEntity.getBody().result;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class CreatedTransaction {

        @JsonProperty("Transaction_Identifier")
        String transactionIdentifier;
        @JsonProperty("Result")
        String result;
        @JsonProperty("ResultMessage")
        String resultMessage;

    }

  /*
  *  getBalanceByAddress(); method is not using for now. It will be available in next up
  */
    public String getBalanceByAddress(String address){
        final MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        requestParameters.add("api_key", "970E22216DA4C486CC22EEF9A58CD30E5B3A8A0D22A62F5D5B57222D16337814CEF3E7B1D7227C4754C733FE39F433F5C4E4E0F8B6D9D8F76F893BBA4");
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("https://api.aisi.io/account/{address}/balance")
                .queryParams(requestParameters)
                .build();
        ResponseEntity<Balance> responseEntity = null;
        try {
            responseEntity = restTemplate.getForEntity(builder.toUriString(), Balance.class, address);
            if (responseEntity.getStatusCodeValue() != 200) {
                log.warn("Error : {}", responseEntity.getStatusCodeValue());
            }
        } catch (Exception ex) {
            log.warn("Error : {}", ex.getMessage());
        }
        return responseEntity.getBody().balance;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    private static class Balance {

        @JsonProperty("Balance")
        String balance;
    }

}
