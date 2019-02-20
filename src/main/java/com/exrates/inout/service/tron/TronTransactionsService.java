package com.exrates.inout.service.tron;

//exrates.model.dto.TronReceivedTransactionDto;

import com.exrates.inout.domain.dto.TronReceivedTransactionDto;

public interface TronTransactionsService {
    boolean checkIsTransactionConfirmed(String txHash);

    void processTransaction(TronReceivedTransactionDto p);

    void processTransaction(int id, String address, String hash, String amount, Integer merchantId, Integer currencyId);
}
