package com.exrates.inout.service;

import com.exrates.inout.service.impl.GapiCurrencyServiceImpl;

import java.util.List;

public interface GapiCurrencyService {

    List<String> generateNewAddress();

    List<GapiCurrencyServiceImpl.Transaction> getAccountTransactions();

    String createNewTransaction(String privKey, String amount);
}
