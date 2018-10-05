package com.exrates.inout.service.cache;

import com.exrates.inout.domain.dto.ExOrderStatisticsShortByPairsDto;

import java.math.BigDecimal;
import java.util.List;

public interface ExchangeRatesHolder {

    void onRatesChange(Integer pairId, BigDecimal rate);

    List<ExOrderStatisticsShortByPairsDto> getAllRates();

    List<ExOrderStatisticsShortByPairsDto> getCurrenciesRates(List<Integer> id);
}
