package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.domain.lisk.LiskOpenAccountDto;
import com.exrates.inout.domain.lisk.LiskSendTxDto;
import com.exrates.inout.domain.lisk.LiskTransaction;
import com.exrates.inout.properties.models.LiskNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.exrates.inout.service.lisk.LiskRestUtils.*;

//exrates.model.dto.merchants.lisk.LiskAccount;
//exrates.model.dto.merchants.lisk.LiskOpenAccountDto;
//exrates.model.dto.merchants.lisk.LiskSendTxDto;
//exrates.model.dto.merchants.lisk.LiskTransaction;


//.exrates.service.lisk.LiskRestUtils.*;

@Log4j2(topic = "lisk_log")
public class LiskRestClientImpl implements LiskRestClient {

    @Autowired
    private RestTemplate restTemplate;

    private String baseUrl;
    private String openAccountUrl;
    private String sendTxUrl;
    private String sortingPrefix;
    private int maxTransactionQueryLimit;
    private JsonNodeType countNodeType;


    private ObjectMapper objectMapper = new ObjectMapper();

    private final String newAccountEndpoint = "/api/accounts/open";
    private final String getAccountByAddressEndpoint = "/api/accounts";
    private final String getTransactionsEndpoint = "/api/transactions";
    private final String getTransactionByIdEndpoint = "/api/transactions/get";
    private final String sendTransactionEndpoint = "/api/transactions";
    private final String getFeeEndpoint = "/api/blocks/getFee";


    @Override
    public void initClient(LiskNode props) {
        String host = props.getHost();
        String mainPort = props.getPort();
        String openAccountPort = props.getPortGetAccount();
        String sendTxPort = props.getPortSendTx();

        this.baseUrl = String.join(":", host, mainPort);
        this.openAccountUrl = String.join(":", host, openAccountPort);
        this.sendTxUrl = String.join(":", host, sendTxPort);
        this.sortingPrefix = props.getTxSortPrefix();
        this.maxTransactionQueryLimit = props.getTxQueryLimit();
        this.countNodeType = props.getTxCountNodeType();
    }

    @Override
    public LiskTransaction getTransactionById(String txId) {
            String response = restTemplate.getForObject(getURIWithParams(absoluteURI(getTransactionByIdEndpoint), Collections.singletonMap("id", txId)),
                    String.class);

            return extractObjectFromResponse(objectMapper, response, "transaction", LiskTransaction.class);
    }

    @Override
    public List<LiskTransaction> getTransactionsByRecipient(String recipientAddress) {
            Map<String, String> params = new HashMap<String, String>() {{
               put("recipientId", recipientAddress);
               put("orderBy", sortingPrefix + "timestamp:asc");
            }};
            String response = restTemplate.getForObject(getURIWithParams(absoluteURI(getTransactionsEndpoint), params),
                    String.class);

            return extractListFromResponse(objectMapper, response, "transactions", LiskTransaction.class);
    }

    @Override
    public List<LiskTransaction> getAllTransactionsByRecipient(String recipientAddress, int offset) {
        log.info("Retrieving transactions: address {} offset {}", recipientAddress, offset);
        List<LiskTransaction> result = new ArrayList<>();
        int newOffset = offset;
        int count;
        do {
            String response = sendGetTransactionsRequest(recipientAddress, newOffset);
            count = Integer.parseInt(extractTargetNodeFromLiskResponse(objectMapper, response, "count", countNodeType).asText());
            result.addAll(extractListFromResponse(objectMapper, response, "transactions", LiskTransaction.class));
            newOffset += result.size();
        } while (newOffset < count);
        return result;
    }

    private String sendGetTransactionsRequest(String recipientAddress, int offset) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("recipientId", recipientAddress);
            put("limit", String.valueOf(maxTransactionQueryLimit));
            put("offset", String.valueOf(offset));
            put("orderBy", sortingPrefix + "timestamp:asc");
        }};
        URI targetURI = getURIWithParams(absoluteURI(getTransactionsEndpoint), params);
        return restTemplate.getForObject(targetURI, String.class);
    }

    @Override
    public Long getFee() {
        String response = restTemplate.getForObject(absoluteURI(getFeeEndpoint), String.class);
        return extractTargetNodeFromLiskResponse(objectMapper, response, "fee", JsonNodeType.NUMBER).longValue();
    }


    @Override
    public String sendTransaction(LiskSendTxDto dto) {
            ResponseEntity<String> response = restTemplate.exchange(sendTxUrl.concat(sendTransactionEndpoint), HttpMethod.PUT, new HttpEntity<>(dto), String.class);
            return extractTargetNodeFromLiskResponse(objectMapper, response.getBody(), "transactionId", JsonNodeType.STRING).textValue();
    }

    @Override
    public LiskAccount createAccount(String secret) {
        LiskOpenAccountDto dto = new LiskOpenAccountDto();
        dto.setSecret(secret);
        ResponseEntity<String> response = restTemplate.exchange(openAccountUrl.concat(newAccountEndpoint), HttpMethod.POST, new HttpEntity<>(dto), String.class);
        return extractObjectFromResponse(objectMapper, response.getBody(), "account", LiskAccount.class);
    }

    @Override
    public LiskAccount getAccountByAddress(String address) {
        String response = restTemplate.getForObject(getURIWithParams(absoluteURI(getAccountByAddressEndpoint), Collections.singletonMap("address", address)),
                String.class);
        return extractObjectFromResponse(objectMapper, response, "account", LiskAccount.class);
    }




    private String absoluteURI(String relativeURI) {
        return String.join("", baseUrl, relativeURI);
    }


}
