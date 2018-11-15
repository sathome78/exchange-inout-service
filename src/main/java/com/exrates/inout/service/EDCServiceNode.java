package com.exrates.inout.service;

import com.exrates.inout.domain.main.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

public interface EDCServiceNode {
  @Transactional
  void rescanUnusedAccounts();

  void submitTransactionsForProcessing(String list);

  String extractAccountId(String account, int invoiceId) throws IOException;

  void transferToMainAccount(String accountId, Transaction tx) throws IOException, InterruptedException;

  void transferFromMainAccount(String accountName, String amount) throws IOException, InterruptedException;
}
