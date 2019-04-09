package com.exrates.inout.service.lisk;


import com.exrates.inout.domain.lisk.ArkSendTxDto;
import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.properties.models.ArkNode;

public interface ArkRpcClient {
    void initClient(ArkNode property);

    LiskAccount createAccount(String secret);

    String createTransaction(ArkSendTxDto dto);

    void broadcastTransaction(String txId);
}
