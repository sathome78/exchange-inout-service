package com.exrates.inout.service.stockExratesRetrieval;

//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;

import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;

import java.util.List;

/**
 * Created by OLEG on 14.12.2016.
 */
public interface StockExrateRetrievalService {
    List<StockExchangeStats> retrieveStats(StockExchange stockExchange);
}
