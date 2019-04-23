package com.exrates.inout.service.stockExratesRetrieval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;

/**
 * Created by OLEG on 20.12.2016.
 */
//@Log4j2(topic = "tracker")
@Service(value = "Binance")
public class BinanceRetrievalService implements StockExrateRetrievalService {

   private static final Logger log = LogManager.getLogger("tracker");

    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;


    @Override
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        List<StockExchangeStats> stockExchangeStatsList = new ArrayList<>();
        stockExchange.getAliasedCurrencyPairs(String::concat)
                .forEach((currencyPairName, currencyPair) -> {
                    String jsonResponse = exchangeResponseProcessingService.sendGetRequest("https://www.binance.com/api/v1/ticker/24hr",
                            Collections.singletonMap("symbol", currencyPairName));
                    stockExchangeStatsList.add(exchangeResponseProcessingService.extractStatsFromSingleNode(jsonResponse,
                            stockExchange, currencyPair.getId())) ;
                });
        return stockExchangeStatsList;


    }

}
