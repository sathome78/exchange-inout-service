package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.CompanyWalletDao;
import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CompanyWalletDaoImpl implements CompanyWalletDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CompanyWalletDaoImpl(@Qualifier(value = "masterTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CompanyWallet create(Currency currency) {
        final String sql = "INSERT INTO COMPANY_WALLET(currency_id) VALUES (:currencyId)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final Map<String, Integer> params = new HashMap<String, Integer>() {{
            put("currencyId", currency.getId());
        }};
        if (jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder) > 0) {
            final CompanyWallet companyWallet = new CompanyWallet();
            companyWallet.setCurrency(currency);
            companyWallet.setId(keyHolder.getKey().intValue());
            return companyWallet;
        }
        return null;
    }

    public CompanyWallet findByCurrencyId(Currency currency) {
        final String sql = "SELECT * FROM  COMPANY_WALLET WHERE currency_id = :currencyId";
        final Map<String, Integer> params = new HashMap<String, Integer>() {{ put("currencyId", currency.getId()); }};
        try {
            return jdbcTemplate.queryForObject(sql, params, new CompanyWallet());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean update(CompanyWallet companyWallet) {
        final String sql = "UPDATE COMPANY_WALLET SET balance = :balance, commission_balance = :commissionBalance where id = :id";
        final Map<String, Object> params = new HashMap<String, Object>() {{
            put("balance", companyWallet.getBalance());
            put("commissionBalance", companyWallet.getCommissionBalance());
            put("id", companyWallet.getId());
        }};
        return jdbcTemplate.update(sql, params) > 0;
    }

    @Override
    public boolean substarctCommissionBalanceById(Integer id, BigDecimal amount) {
        String sql = "UPDATE COMPANY_WALLET " +
                " SET commission_balance = commission_balance - :amount" +
                " WHERE id = :company_wallet_id ";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("company_wallet_id", id);
            put("amount", amount);
        }};
        return jdbcTemplate.update(sql, params) > 0;
    }
}