package com.exrates.inout.service.impl;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.OrderService;
import com.exrates.inout.service.RatesHolder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Log4j2
@Component
public class RatesHolderImpl implements RatesHolder {

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private OrderService orderService;

    /*contains currency pairId and its rate*/
    private Table<Integer, OperationType, BigDecimal> ratesMap = HashBasedTable.create();

    @PostConstruct
    public void init() {

    }

    @Override
    public void onRateChange(int pairId, OperationType operationType, BigDecimal rate) {
        ratesMap.put(pairId, OperationType.BUY, rate);
        ratesMap.put(pairId, OperationType.SELL, rate);
    }

    @Override
    public BigDecimal getCurrentRate(int pairId, OperationType operationType) {
        return ratesMap.get(pairId, operationType);
    }



}
