package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.LiskAccount;
import com.exrates.inout.domain.lisk.LiskSendTxDto;
import com.exrates.inout.domain.lisk.LiskTransaction;
import com.exrates.inout.properties.models.LiskProperty;

import java.util.List;

public interface LiskRestClient {

    void initClient(LiskProperty property);

    LiskTransaction getTransactionById(String txId);

    List<LiskTransaction> getTransactionsByRecipient(String recipientAddress);

    List<LiskTransaction> getAllTransactionsByRecipient(String recipientAddress, int offset);

    Long getFee();

    String sendTransaction(LiskSendTxDto dto);

    LiskAccount createAccount(String secret);

    LiskAccount getAccountByAddress(String address);
}
