package com.exrates.inout.dao;

import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;

public interface CompanyWalletDao {

    CompanyWallet create(Currency currency);

    CompanyWallet findByCurrencyId(Currency currency);

    boolean update(CompanyWallet companyWallet);
}