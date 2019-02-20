package com.exrates.inout.service.stopOrder;

import me.exrates.model.enums.OperationType;

import java.math.BigDecimal;

/**
 * Created by maks on 22.04.2017.
 */
public interface RatesHolder {

    void onRateChange(int pairId, OperationType operationType, BigDecimal rate);

    BigDecimal getCurrentRate(int pairId, OperationType operationType);
}
