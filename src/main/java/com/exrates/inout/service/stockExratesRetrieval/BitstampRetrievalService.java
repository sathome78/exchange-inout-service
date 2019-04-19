package com.exrates.inout.service.stockExratesRetrieval;

import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;

/**
 * Created by OLEG on 20.12.2016.
 */
//@Log4j2(topic = "tracker")
@Service(value = "Bitstamp")
public class BitstampRetrievalService implements StockExrateRetrievalService {

    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;


    @Override
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        List<StockExchangeStats> stockExchangeStatsList = new ArrayList<>();
        stockExchange.getAliasedCurrencyPairs((name1, name2) -> name1.toLowerCase() + name2.toLowerCase())
                .forEach((currencyPairName, currencyPair) -> {
            String url = "https://www.bitstamp.net/api/v2/ticker/" + currencyPairName + "/";
            String jsonResponse = exchangeResponseProcessingService.sendGetRequest(url);
            stockExchangeStatsList.add(exchangeResponseProcessingService.extractStatsFromSingleNode(jsonResponse,
                            stockExchange, currencyPair.getId())) ;
        });
        return stockExchangeStatsList;


    }

}
