package com.exrates.inout.service.aisi;

import java.math.BigDecimal;
import java.util.List;

import com.exrates.inout.service.aisi.AisiCurrencyServiceImpl.Transaction;

public interface AisiCurrencyService {

    String generateNewAddress();

    String getBalanceByAddress(String address);

    List<Transaction> getAccountTransactions();

    String createNewTransaction(String address, BigDecimal amount);

}
