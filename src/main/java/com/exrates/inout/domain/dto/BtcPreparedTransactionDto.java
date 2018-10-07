package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class BtcPreparedTransactionDto {
    private Map<String, BigDecimal> payments;
    private BigDecimal feeAmount;
    private String hex;

    public BtcPreparedTransactionDto(Map<String, BigDecimal> payments, BigDecimal feeAmount, String hex) {
        this.payments = payments;
        this.feeAmount = feeAmount;
        this.hex = hex;
    }
}
