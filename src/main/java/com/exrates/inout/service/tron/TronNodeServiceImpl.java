package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.TronNewAddressDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Log4j2(topic = "tron")
@Service
public class TronNodeServiceImpl implements TronNodeService {

    private final static String GET_ADDRESS = "/wallet/generateaddress";
    private final static String EASY_TRANSFER = "/wallet/easytransferbyprivate";
    private final static String GET_BLOCK_TX = "/wallet/getblockbynum";
    private final static String GET_TX = "/api/transfer/";
    private final static String GET_LAST_BLOCK = "/wallet/getnowblock";
    private final static String GET_ACCOUNT_INFO = "/api/grpc/full/getaccount/";

    private final CryptoCurrencyProperties ccp;
    private final RestTemplate restTemplate;

    @Autowired
    public TronNodeServiceImpl(CryptoCurrencyProperties ccp,
                               RestTemplate restTemplate) {
        this.ccp = ccp;
        this.restTemplate = restTemplate;
    }

    @Override
    public TronNewAddressDto getNewAddress() {
        String url = ccp.getOtherCoins().getTron().getFullNodeUrl().concat(GET_ADDRESS);
        log.debug("trx url " + url);
        return TronNewAddressDto.fromGetAddressMethod(restTemplate.postForObject(url, null, String.class));
    }

    @SneakyThrows
    @Override
    public JSONObject transferFunds(TronTransferDto tronTransferDto) {
        String url = ccp.getOtherCoins().getTron().getFullNodeUrl().concat(EASY_TRANSFER);
        RequestEntity<TronTransferDto> requestEntity = new RequestEntity<>(tronTransferDto, HttpMethod.POST, new URI(url));
        return new JSONObject(performRequest(requestEntity));
    }

    @SneakyThrows
    @Override
    public JSONObject getTransactions(long blockNum) {
        String url = ccp.getOtherCoins().getTron().getFullNodeUrl().concat(GET_BLOCK_TX);
        JSONObject object = new JSONObject() {{
            put("num", blockNum);
        }};
        RequestEntity<String> requestEntity = new RequestEntity<>(object.toString(), HttpMethod.POST, new URI(url));
        return new JSONObject(performRequest(requestEntity));
    }

    @SneakyThrows
    @Override
    public JSONObject getTransaction(String hash) {
        String url = String.join("", ccp.getOtherCoins().getTron().getSolidityNodeUrl(), GET_TX, hash);
        RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, new URI(url));
        return new JSONObject(performRequest(requestEntity));
    }

    @SneakyThrows
    @Override
    public JSONObject getLastBlock() {
        String url = ccp.getOtherCoins().getTron().getFullNodeUrl().concat(GET_LAST_BLOCK);
        RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.POST, new URI(url));
        return new JSONObject(performRequest(requestEntity));
    }

    @SneakyThrows
    @Override
    public JSONObject getAccount(String addressBase58) {
        String url = String.join("", ccp.getOtherCoins().getTron().getSolidityNodeUrl(), GET_ACCOUNT_INFO, addressBase58);
        RequestEntity<String> requestEntity = new RequestEntity<>(HttpMethod.GET, new URI(url));
        return new JSONObject(performRequest(requestEntity));
    }

    private String performRequest(RequestEntity requestEntity) {
        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(requestEntity, String.class);
            log.debug("trx response to url {} - {}", requestEntity.getUrl(), responseEntity);
            return new String(responseEntity.getBody().getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("trx request {} {} {}", requestEntity.getUrl(), requestEntity.getMethod(), e);
            throw new RuntimeException(e);
        }
    }
}
