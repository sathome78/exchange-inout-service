package com.exrates.inout.service.tron;

public interface TronTransactionsService {

    boolean checkIsTransactionConfirmed(String txHash);

    void processTransaction(String address, String hash, String amount);
}
