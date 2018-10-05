package com.exrates.inout.dao;

import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;

import java.math.BigDecimal;

public interface CompanyWalletDao {

    CompanyWallet create(Currency currency);

    CompanyWallet findByCurrencyId(Currency currency);

    boolean update(CompanyWallet companyWallet);

    boolean substarctCommissionBalanceById(Integer id, BigDecimal amount);
}