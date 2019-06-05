package com.exrates.inout.service.aisi;

import com.exrates.inout.service.aisi.AisiCurrencyServiceImpl.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface AisiCurrencyService {

    String generateNewAddress();

    String getBalanceByAddress(String address);

    List<Transaction> getAccountTransactions();

    String createNewTransaction(String address, BigDecimal amount);

}
