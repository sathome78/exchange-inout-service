package com.exrates.inout.dao;

import com.exrates.inout.domain.main.Transaction;

public interface TransactionDao {

    Transaction create(Transaction transaction);

    boolean updateForProvided(Transaction transaction);

    Transaction findById(int id);

    boolean setStatusById(Integer trasactionId, Integer statusId);
}
