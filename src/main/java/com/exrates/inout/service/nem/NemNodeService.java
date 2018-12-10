package com.exrates.inout.service.nem;

import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.InvalidAccountException;
import com.exrates.inout.exceptions.NemTransactionException;
import com.exrates.inout.exceptions.NisNotReadyException;
import com.exrates.inout.exceptions.NisTransactionException;
import com.exrates.inout.util.RestUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nem.core.time.TimeInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2(topic = "nem_log")
@Service
public class NemNodeService {

    private final static String PATH_EXTENDED_INFO = "/node/extended-info";
    private final static String PATH_PREPARE_ANOUNCE = "/transaction/prepare-announce";
    private final static String PATH_GET_TRANSACTION = "/transaction/get?hash=";
    private final static String PATH_GET_CURRENT_BLOCK_HEIGHT = "/chain/last-block";
    private final static String PATH_GET_INCOME_TRANSACTIONS = "/account/transfers/incoming?address=%s";
    private final static String PATH_GET_OWNED_MOSAICS = "/account/mosaic/owned?address=%s";
    private final static String PATH_GET_ADDRESS_BY_PK = "/account/get/from-public-key?publicKey=%s";

    @Value("${nem.node.ncc.server-url}")
    private String nccServer;
    @Value("${nem.node.nis.server-url-receive}")
    private String nisServerRecieve;
    @Value("${nem.node.nis.server-url-send}")
    private String nisServerSend;

    @Autowired
    private RestTemplate restTemplate;

    private JSONObject getNodeExtendedInfo() {
        String response = restTemplate.getForObject(nisServerRecieve.concat(PATH_EXTENDED_INFO), String.class);
        return new org.json.JSONObject(response);
    }

    String getAddressByPk(String publicKey) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(nisServerRecieve.concat(String.format(PATH_GET_ADDRESS_BY_PK, publicKey)), String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody()).getJSONObject("account").getString("address");
    }

    JSONArray getOwnedMosaics(String address) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(nisServerRecieve.concat(String.format(PATH_GET_OWNED_MOSAICS, address)), String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody()).getJSONArray("data");
    }

    TimeInstant getCurrentTimeStamp() {
        try {
            int time = getNodeExtendedInfo().getJSONObject("nisInfo").getInt("currentTime");
            return new TimeInstant(time);
        } catch (Exception e) {
            log.error(e);
            throw new NisNotReadyException();
        }
    }

    JSONObject anounceTransaction(String serializedTransaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<>(serializedTransaction, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(nisServerSend.concat(PATH_PREPARE_ANOUNCE), entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        if (RestUtil.isError(response.getStatusCode())) {
            String error = result.getString("message");
            try {
                defineAndThrowException(error);
            } catch (RuntimeException e) {
                log.error("response {}, {}", response, e);
                throw e;
            }
        }
        return result;
    }

    JSONObject getSingleTransactionByHash(String hash) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(nisServerRecieve.concat(PATH_GET_TRANSACTION).concat(hash), String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody());
    }

    JSONArray getIncomeTransactions(String address, String hash) {
        String url = nisServerRecieve.concat(String.format(PATH_GET_INCOME_TRANSACTIONS, address));
        if (!StringUtils.isEmpty(hash)) {
            url = url.concat("&hash=").concat(hash);
        }
        ResponseEntity<String> response = restTemplate
                .getForEntity(url, String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody()).getJSONArray("data");
    }

    long getLastBlockHeight() {
        String response = restTemplate.getForObject(nisServerRecieve.concat(PATH_GET_CURRENT_BLOCK_HEIGHT), String.class);
        return new org.json.JSONObject(response).getLong("height");
    }

    private void defineAndThrowException(String errorMessage) {
        switch (errorMessage) {
            case "address must be valid": {
                throw new InvalidAccountException(errorMessage);
            }
            case "FAILURE_INSUFFICIENT_BALANCE": {
                throw new InsufficientCostsInWalletException("NEM BALANCE LOW");
            }
            default:
                throw new NisTransactionException(errorMessage);
        }
    }
}
