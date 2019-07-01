package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.domain.lisk.LiskSendTxDto;


public class LiskSpecialMethodServiceImpl implements LiskSpecialMethodService {

    private LiskRestClient liskRestClient;

    public LiskSpecialMethodServiceImpl(LiskRestClient liskRestClient) {
        this.liskRestClient = liskRestClient;
    }

    @Override
    public String sendTransaction(String secret, Long amount, String recipientId) {
        LiskSendTxDto dto = new LiskSendTxDto();
        dto.setPassphrase(secret);
        dto.setAmount(amount);
        dto.setRecipientId(recipientId);
        return liskRestClient.sendTransaction(dto);
    }

    @Override
    public LiskAccount createAccount(String secret) {
        return liskRestClient.createAccount(secret);
    }
}
