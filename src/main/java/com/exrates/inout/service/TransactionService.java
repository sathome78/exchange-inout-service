package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Transaction;
import java.util.List;

public interface TransactionService {

    Transaction createTransactionRequest(CreditsOperation creditsOperation);

    Transaction findById(int id);

    void provideTransaction(Transaction transaction);

    void setSourceId(Integer trasactionId, Integer sourceId);

    List<UserSummaryOrdersDto> getUserSummaryOrdersList(Integer requesterUserId, String startDate, String endDate, List<Integer> roles);

    List<Transaction> getPayedRefTransactionsByOrderId(int orderId);

    boolean setStatusById(int transactionId, int status);
}
