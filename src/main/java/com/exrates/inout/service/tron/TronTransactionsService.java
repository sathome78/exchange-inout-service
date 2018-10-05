package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.TronReceivedTransactionDto;

public interface TronTransactionsService {
    boolean checkIsTransactionConfirmed(String txHash);

    void processTransaction(TronReceivedTransactionDto p);

    void processTransaction(String address, String hash, String amount);
}
