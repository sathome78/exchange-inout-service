package com.exrates.inout.service.nem;

import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.InvalidAccountException;
import com.exrates.inout.exceptions.NemTransactionException;
import com.exrates.inout.exceptions.NisNotReadyException;
import com.exrates.inout.exceptions.NisTransactionException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.NemProperty;
import com.exrates.inout.util.RestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.nem.core.time.TimeInstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//exrates.service.exception.NemTransactionException;
//exrates.service.exception.NisNotReadyException;
//exrates.service.exception.NisTransactionException;
//exrates.service.exception.invoice.InsufficientCostsInWalletException;
//exrates.service.exception.invoice.InvalidAccountException;
//exrates.service.util.RestUtil;

/**
 * Created by maks on 20.07.2017.
 */
//@Log4j2(topic = "nem_log")
@Service
public class NemNodeService {

   private static final Logger log = LogManager.getLogger("nem_log");

    private final static String pathExtendedInfo = "/node/extended-info";
    private final static String pathPrepareAnounce = "/transaction/prepare-announce";
    private final static String pathGetTransaction = "/transaction/get?hash=";
    private final static String pathGetCurrentBlockHeight = "/chain/last-block";
    private final static String pathGetIncomeTransactions = "/account/transfers/incoming?address=%s";
    private final static String pathGetOwnedMosaics = "/account/mosaic/owned?address=%s";
    private final static String pathGetAddressByPk = "/account/get/from-public-key?publicKey=%s";


    @Autowired
    private RestTemplate restTemplate;

    private final NemProperty property;

    public NemNodeService(CryptoCurrencyProperties ccp) {
        this.property = ccp.getNemCoins().getNem();
    }

    private JSONObject getNodeExtendedInfo() {
        String response = restTemplate.getForObject(property.getNisServerUrlReceive().concat(pathExtendedInfo), String.class);
        return new org.json.JSONObject(response);
    }

    String getAddressByPk(String publicKey) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(property.getNisServerUrlReceive().concat(String.format(pathGetAddressByPk, publicKey)), String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody()).getJSONObject("account").getString("address");
    }

    JSONArray getOwnedMosaics(String address) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(property.getNisServerUrlReceive().concat(String.format(pathGetOwnedMosaics, address)), String.class);
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
        HttpEntity<String> entity = new HttpEntity<>(serializedTransaction ,headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(property.getNisServerUrlReceive().concat(pathPrepareAnounce), entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        if (RestUtil.isError(response.getStatusCode())) {
            String error = result.getString("message");
            try {
                defineAndThrowException(error);
            } catch (RuntimeException e) {
                log.error("response {}, {}",response, e);
                throw e;
            }
        }

        return result;
    }

    JSONObject getSingleTransactionByHash(String hash) {
        ResponseEntity<String> response = restTemplate
                .getForEntity(property.getNisServerUrlReceive().concat(pathGetTransaction).concat(hash), String.class);
        if (RestUtil.isError(response.getStatusCode()) || response.getBody().contains("error")) {
            throw new NemTransactionException(response.toString());
        }
        return new JSONObject(response.getBody());
    }

    JSONArray getIncomeTransactions(String address, String hash) {
        String url = property.getNisServerUrlReceive().concat(String.format(pathGetIncomeTransactions, address));
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
        String response = restTemplate.getForObject(property.getNisServerUrlReceive().concat(pathGetCurrentBlockHeight), String.class);
        return new org.json.JSONObject(response).getLong("height");
    }


    private void defineAndThrowException(String errorMessage) {
        switch (errorMessage) {
            case "address must be valid" : {
                throw new InvalidAccountException(errorMessage);
            }
            case "FAILURE_INSUFFICIENT_BALANCE" : {
                throw new InsufficientCostsInWalletException("NEM BALANCE LOW");
            }
            default: throw new NisTransactionException(errorMessage);
        }
    }


}
