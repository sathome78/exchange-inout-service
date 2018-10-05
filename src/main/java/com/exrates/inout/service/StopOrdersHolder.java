package com.exrates.inout.service;

import com.exrates.inout.domain.dto.StopOrderSummaryDto;
import com.exrates.inout.domain.main.ExOrder;

import java.math.BigDecimal;
import java.util.NavigableSet;

public interface StopOrdersHolder {
    NavigableSet<StopOrderSummaryDto> getSellOrdersForPairAndStopRate(int pairId, BigDecimal rate);

    NavigableSet<StopOrderSummaryDto> getBuyOrdersForPairAndStopRate(int pairId, BigDecimal rate);

    void delete(int pairId, StopOrderSummaryDto summaryDto);

    void addOrder(ExOrder exOrder);
}
