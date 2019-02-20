package com.exrates.inout.service.cache;

import me.exrates.model.ExOrder;
import me.exrates.model.dto.onlineTableDto.ExOrderStatisticsShortByPairsDto;
import me.exrates.model.enums.TradeMarket;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ExchangeRatesHolder {

    void onRatesChange(ExOrder exOrder);

    ExOrderStatisticsShortByPairsDto getOne(Integer id);

    List<ExOrderStatisticsShortByPairsDto> getAllRates();

    List<ExOrderStatisticsShortByPairsDto> getCurrenciesRates(List<Integer> id);

    Map<Integer, String> getRatesForMarket(TradeMarket market);

    BigDecimal getBtcUsdRate();
}
