package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.UserSummaryOrdersDto;
import com.exrates.inout.domain.main.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionDao {

    Transaction create(Transaction transaction);

    boolean updateForProvided(Transaction transaction);

    Transaction findById(int id);

    boolean provide(int id);

    boolean delete(int id);

    BigDecimal maxAmount();

    void setSourceId(Integer trasactionId, Integer sourceId);

    boolean setStatusById(Integer trasactionId, Integer statusId);

    List<UserSummaryOrdersDto> getUserSummaryOrdersList(Integer requesterUserId, String startDate, String endDate, List<Integer> roles);

    List<Transaction> getPayedRefTransactionsByOrderId(int orderId);
}
