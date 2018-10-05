package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.ArkSendTxDto;
import com.exrates.inout.domain.lisk.LiskAccount;

public interface ArkRpcClient {
    void initClient(String propertySource);

    LiskAccount createAccount(String secret);

    String createTransaction(ArkSendTxDto dto);

    void broadcastTransaction(String txId);
}
