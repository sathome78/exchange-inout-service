package com.exrates.inout.service;

import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Transaction;

public interface TransactionService {

    Transaction createTransactionRequest(CreditsOperation creditsOperation);

    Transaction findById(int id);

    void provideTransaction(Transaction transaction);

    void setSourceId(Integer trasactionId, Integer sourceId);

    boolean setStatusById(Integer transactionId, Integer status);
}
