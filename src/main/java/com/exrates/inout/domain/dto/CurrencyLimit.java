package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyLimit {

    private int id;
    private Currency currency;
    private OperationType operationType;
    private BigDecimal minSum;
    private BigDecimal maxSum;
    private Integer maxDailyRequest;
    private BigDecimal currencyUsdRate;
    private BigDecimal minSumUsdRate;
    private boolean recalculateToUsd;
}
