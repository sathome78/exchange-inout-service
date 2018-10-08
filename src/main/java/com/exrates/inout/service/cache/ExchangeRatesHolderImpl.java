package com.exrates.inout.service.cache;

import com.exrates.inout.dao.OrderDao;
import com.exrates.inout.domain.dto.ExOrderStatisticsShortByPairsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ExchangeRatesHolderImpl implements ExchangeRatesHolder {

    private Map<Integer, ExOrderStatisticsShortByPairsDto> ratesMap = new ConcurrentHashMap<>();

    private final OrderDao orderDao;

    @Autowired
    public ExchangeRatesHolderImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @PostConstruct
    private void init() {
        List<ExOrderStatisticsShortByPairsDto> list = orderDao.getOrderStatisticByPairs();
        list.forEach(p-> ratesMap.put(p.getCurrencyPairId(), p));
    }

    public List<ExOrderStatisticsShortByPairsDto> getAllRates() {
        return new ArrayList<>(ratesMap.values());
    }

    public List<ExOrderStatisticsShortByPairsDto> getCurrenciesRates(List<Integer> id) {
        if (id == null || id.isEmpty()) {
            return Collections.emptyList();
        }
        List<ExOrderStatisticsShortByPairsDto> result = new ArrayList<>();
        id.forEach(p-> result.add(ratesMap.get(p)));
        return result;
    }
}
