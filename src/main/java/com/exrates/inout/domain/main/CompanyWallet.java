package com.exrates.inout.domain.main;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyWallet {
    private int id;
    private Currency currency;
    private BigDecimal balance;
    private BigDecimal commissionBalance;
}