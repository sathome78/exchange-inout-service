package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.ArkOpenAccountDto;
import com.exrates.inout.domain.lisk.ArkSendTxDto;
import com.exrates.inout.domain.lisk.LiskAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

import static com.exrates.inout.service.lisk.LiskRestUtils.extractObjectFromResponse;
import static com.exrates.inout.service.lisk.LiskRestUtils.extractTargetNodeFromLiskResponse;

//.exrates.service.lisk.LiskRestUtils.extractObjectFromResponse;
//.exrates.service.lisk.LiskRestUtils.extractTargetNodeFromLiskResponse;

//exrates.model.dto.merchants.lisk.ArkOpenAccountDto;
//exrates.model.dto.merchants.lisk.ArkSendTxDto;
//exrates.model.dto.merchants.lisk.LiskAccount;

@Log4j2(topic = "lisk_log")
@Service
@Scope("prototype")
public class ArkRpcClientImpl implements ArkRpcClient {
    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();


    private String openAccountUrl;
    private String createTxUrl;
    private String broadcastTxUrl;


    @Override
    public void initClient(String propertySource) {
        Properties props = new Properties();
        try {
            props.load(getClass().getClassLoader().getResourceAsStream(propertySource));
            String host = props.getProperty("lisk.node.host");
            String openAccountPort = props.getProperty("lisk.port.getAccount");
            String sendTxPort = props.getProperty("lisk.port.sendTx");
            String createAccountEndpoint = props.getProperty("ark.createAccountEndpoint");
            String createTxEndpoint = props.getProperty("ark.createTxEndpoint");
            String broadcastTxEndpoint = props.getProperty("ark.broadcastTxEndpoint");

            this.openAccountUrl = String.join("", host, ":", openAccountPort, createAccountEndpoint);
            this.createTxUrl = String.join("", host, ":", sendTxPort, createTxEndpoint);
            this.broadcastTxUrl = String.join("", host, ":", sendTxPort, broadcastTxEndpoint);

        } catch (IOException e) {
            //log.error(e);
        }
    }



    @Override
    public LiskAccount createAccount(String secret) {
        ArkOpenAccountDto dto = new ArkOpenAccountDto();
        dto.setPassphrase(secret);
        ResponseEntity<String> response = restTemplate.exchange(openAccountUrl, HttpMethod.POST, new HttpEntity<>(dto), String.class);
        return extractObjectFromResponse(objectMapper, response.getBody(), "account", LiskAccount.class);
    }

    @Override
    public String createTransaction(ArkSendTxDto dto) {
        ResponseEntity<String> response = restTemplate.exchange(createTxUrl, HttpMethod.POST, new HttpEntity<>(dto), String.class);
        return extractTargetNodeFromLiskResponse(objectMapper, response.getBody(), "transaction", JsonNodeType.OBJECT)
                .get("id").textValue();
    }

    @Override
    public void broadcastTransaction(String txId) {
        restTemplate.exchange(broadcastTxUrl, HttpMethod.POST, new HttpEntity<>(Collections.singletonMap("id", txId)), String.class);
    }

}
