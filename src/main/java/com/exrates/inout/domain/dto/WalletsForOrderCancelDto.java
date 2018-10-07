package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletsForOrderCancelDto {
    private int orderId;
    private int orderStatusId;
    private BigDecimal reservedAmount;
    private int walletId;
    private BigDecimal activeBalance;
    private BigDecimal reservedBalance;
}
