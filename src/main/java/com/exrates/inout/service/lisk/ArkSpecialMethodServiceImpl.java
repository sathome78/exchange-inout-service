package com.exrates.inout.service.lisk;

//exrates.model.dto.merchants.lisk.ArkSendTxDto;
//exrates.model.dto.merchants.lisk.LiskAccount;

import com.exrates.inout.domain.lisk.ArkSendTxDto;
import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.properties.models.ArkNode;

public class ArkSpecialMethodServiceImpl implements LiskSpecialMethodService {

    private ArkRpcClient arkRpcClient;

    private final Object SEND_TX_LOCK = new Object();

    private ArkNode arkNodeProperty;

    public ArkSpecialMethodServiceImpl(ArkNode arkNodeProperty, ArkRpcClient arkRpcClient) {
        this.arkRpcClient = arkRpcClient;
        this.arkRpcClient.initClient(arkNodeProperty);
    }

    @Override
    public String sendTransaction(String secret, Long amount, String recipientId) {
        ArkSendTxDto dto = new ArkSendTxDto();
        dto.setPassphrase(secret);
        dto.setAmount(amount);
        dto.setRecipientId(recipientId);
        String txId;
        synchronized (SEND_TX_LOCK) {
            txId = arkRpcClient.createTransaction(dto);
            arkRpcClient.broadcastTransaction(txId);
        }
        return txId;
    }

    @Override
    public LiskAccount createAccount(String secret) {
        return arkRpcClient.createAccount(secret);
    }
}
