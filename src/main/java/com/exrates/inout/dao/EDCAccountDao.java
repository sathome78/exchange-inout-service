package com.exrates.inout.dao;

import com.exrates.inout.domain.main.EDCAccount;

import java.util.List;

public interface EDCAccountDao {

    EDCAccount findByTransactionId(int id);

    void create(EDCAccount edcAccount);

    List<EDCAccount> getUnusedAccounts();

    void setAccountUsed(int transactionId);
}
