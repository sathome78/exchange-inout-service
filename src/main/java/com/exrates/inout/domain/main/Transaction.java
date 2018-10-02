package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class Transaction {
    private int id;
    private Wallet userWallet;
    private CompanyWallet companyWallet;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private Commission commission;
    private OperationType operationType;
    private Currency currency;
    private Merchant merchant;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime datetime;
    private ExOrder order;
    private boolean provided;
    private Integer confirmation;
    private BigDecimal activeBalanceBefore;
    private BigDecimal reservedBalanceBefore;
    private BigDecimal companyBalanceBefore;
    private BigDecimal companyCommissionBalanceBefore;
    private TransactionSourceType sourceType;
    private Integer sourceId;
    private String description;
    private WithdrawRequest withdrawRequest;
    private RefillRequest refillRequest;
}