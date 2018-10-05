package com.exrates.inout.service;

import com.exrates.inout.domain.enums.OperationType;

import java.math.BigDecimal;

public interface RatesHolder {

    void onRateChange(int pairId, OperationType operationType, BigDecimal rate);

    BigDecimal getCurrentRate(int pairId, OperationType operationType);
}
