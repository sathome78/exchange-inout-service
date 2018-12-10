package com.exrates.inout.service.qtum;


import com.exrates.inout.domain.qtum.Block;
import com.exrates.inout.domain.qtum.QtumJsonRpcRequest;
import com.exrates.inout.domain.qtum.QtumJsonRpcResponse;
import com.exrates.inout.domain.qtum.QtumJsonRpcResponseList;
import com.exrates.inout.domain.qtum.QtumListTransactions;
import com.exrates.inout.domain.qtum.QtumTokenContract;
import com.exrates.inout.domain.qtum.QtumTokenTransaction;
import com.exrates.inout.domain.qtum.QtumTransaction;
import com.exrates.inout.exceptions.QtumApiException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2(topic = "qtum_log")
public class QtumNodeServiceImpl implements QtumNodeService {

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor(ccp.getOtherCoins().getQtum().getUser(), ccp.getOtherCoins().getQtum().getPassword()));
    }

    @Override
    public String getNewAddress() {
        return invokeJsonRpcMethod("getnewaddress", Collections.emptyList(), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public String getBlockHash(Integer height) {
        return invokeJsonRpcMethod("getblockhash", Collections.singletonList(height), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public Block getBlock(String hash) {
        return invokeJsonRpcMethod("getblock", Collections.singletonList(hash), new TypeReference<QtumJsonRpcResponse<Block>>() {
        });
    }

    @Override
    public Optional<QtumListTransactions> listSinceBlock(String blockHash) {
        try {
            return Optional.of(invokeJsonRpcMethod("listsinceblock", Collections.singletonList(blockHash), new TypeReference<QtumJsonRpcResponse<QtumListTransactions>>() {
            }));
        } catch (Exception e) {
            log.error(e);
            return Optional.empty();
        }
    }

    @Override
    public void setWalletPassphrase() {
        invokeJsonRpcMethod("walletpassphrase", Arrays.asList(ccp.getOtherCoins().getQtum().getWalletPassword(), 2), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public BigDecimal getBalance() {
        return invokeJsonRpcMethod("getbalance", Collections.emptyList(), new TypeReference<QtumJsonRpcResponse<BigDecimal>>() {
        });
    }

    @Override
    public void transfer(String address, BigDecimal amount) {
        invokeJsonRpcMethod("sendtoaddress", Arrays.asList(address, amount), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public void backupWallet() {
        invokeJsonRpcMethod("backupwallet", Collections.singletonList(ccp.getOtherCoins().getQtum().getBackupFolder()), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public QtumTransaction getTransaction(String hash) {
        return invokeJsonRpcMethod("gettransaction", Collections.singletonList(hash), new TypeReference<QtumJsonRpcResponse<QtumTransaction>>() {
        });
    }

    @Override
    public List<QtumTokenTransaction> getTokenHistory(Integer blockStart, List<String> tokenAddressList) {
        return invokeJsonRpcMethodList(
                "searchlogs",
                Arrays.asList(
                        blockStart,
                        -1,
                        Collections.singletonMap("addresses", tokenAddressList),
                        Collections.singletonList("ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef")),
                new TypeReference<QtumJsonRpcResponseList<QtumTokenTransaction>>() {
                });
    }

    @Override
    public String fromHexAddress(String address) {
        return invokeJsonRpcMethod("fromhexaddress", Collections.singletonList(address), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public String getHexAddress(String address) {
        return invokeJsonRpcMethod("gethexaddress", Collections.singletonList(address), new TypeReference<QtumJsonRpcResponse<String>>() {
        });
    }

    @Override
    public QtumTokenContract getTokenBalance(String tokenAddress, String data) {
        return invokeJsonRpcMethod("callcontract", Arrays.asList(tokenAddress, data), new TypeReference<QtumJsonRpcResponse<QtumTokenContract>>() {
        });
    }

    @Override
    public void sendToContract(String tokenAddress, String data, String addressFrom) {
        invokeJsonRpcMethod("sendtocontract", Arrays.asList(tokenAddress, data, 0, 250000, "0.0000004", addressFrom), new TypeReference<QtumJsonRpcResponse<QtumTransaction>>() {
        });
    }

    private <T> T invokeJsonRpcMethod(String methodName, List<Object> args, TypeReference<QtumJsonRpcResponse<T>> typeReference) {
        QtumJsonRpcRequest request = new QtumJsonRpcRequest();
        request.setMethod(methodName);
        request.setParams(args);
        return getQtumJsonRpcResponse(request, typeReference);
    }

    private <T> List<T> invokeJsonRpcMethodList(String methodName, List<Object> args, TypeReference<QtumJsonRpcResponseList<T>> typeReference) {
        QtumJsonRpcRequest request = new QtumJsonRpcRequest();
        request.setMethod(methodName);
        request.setParams(args);
        return getQtumJsonRpcResponseList(request, typeReference);
    }

    private <T> T getQtumJsonRpcResponse(QtumJsonRpcRequest request, TypeReference<QtumJsonRpcResponse<T>> typeReference) {
        try {
            String responseString = restTemplate.postForObject(ccp.getOtherCoins().getQtum().getEndpoint(), request, String.class);
            QtumJsonRpcResponse<T> response = objectMapper.readValue(responseString, typeReference);
            if (response.getError() != null) {
                log.error(response.getError());
                throw new QtumApiException(response.getError().getCode(), response.getError().getMessage());
            }
            if (response.getResult() == null && !request.getMethod().equals("walletpassphrase")) {
                throw new QtumApiException("No result found in response");
            }
            return response.getResult();
        } catch (HttpStatusCodeException e) {
            throw new QtumApiException(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e);
            throw new QtumApiException(e);
        }
    }

    private <T> List<T> getQtumJsonRpcResponseList(QtumJsonRpcRequest request, TypeReference<QtumJsonRpcResponseList<T>> typeReference) {
        String responseString = restTemplate.postForObject(ccp.getOtherCoins().getQtum().getEndpoint(), request, String.class);
        try {
            QtumJsonRpcResponseList<T> response = objectMapper.readValue(responseString, typeReference);
            if (response.getError() != null) {
                log.error(response.getError());
                throw new QtumApiException(response.getError().getCode(), response.getError().getMessage());
            }
            if (response.getResult() == null && !request.getMethod().equals("walletpassphrase")) {
                throw new QtumApiException("No result found in response");
            }
            return response.getResult();
        } catch (IOException e) {
            throw new QtumApiException(e);
        }
    }
}
