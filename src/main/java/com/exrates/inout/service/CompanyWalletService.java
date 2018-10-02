package com.exrates.inout.service;

import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;

import java.math.BigDecimal;

public interface CompanyWalletService {

    CompanyWallet create(Currency currency);

    CompanyWallet findByCurrency(Currency currency);

    void withdraw(CompanyWallet companyWallet, BigDecimal amount, BigDecimal commissionAmount);

    void deposit(CompanyWallet companyWallet, BigDecimal amount, BigDecimal commissionAmount);

}