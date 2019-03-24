package com.exrates.inout.service;

import com.exrates.inout.domain.main.Transaction;

import java.io.IOException;

public interface EDCServiceNode {

    void submitTransactionsForProcessing(String list);

    String extractAccountId(final String account, final int invoiceId) throws IOException;

    void rescanUnusedAccounts();

    void transferToMainAccount(String accountId, Transaction tx) throws IOException, InterruptedException;

    void transferFromMainAccount(String accountName, String amount) throws IOException, InterruptedException;

    String extractBalance(final String accountId, final int invoiceId) throws IOException;
}