package com.exrates.inout.domain.main;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class CompanyWallet implements RowMapper<CompanyWallet> {
    private int id;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal commissionBalance;

    @Override
    public CompanyWallet mapRow(ResultSet resultSet, int i) throws SQLException {
        CompanyWallet companyWallet = new CompanyWallet();

        companyWallet.setId(resultSet.getInt("id"));
        companyWallet.setBalance(resultSet.getBigDecimal("balance"));
        companyWallet.setCommissionBalance(resultSet.getBigDecimal("commission_balance"));
        companyWallet.setCurrency(currency);
        
        return companyWallet;
    }
}