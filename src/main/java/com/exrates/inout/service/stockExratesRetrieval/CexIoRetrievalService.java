package com.exrates.inout.service.stockExratesRetrieval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;

/**
 * Created by OLEG on 20.12.2016.
 */
//@Log4j2(topic = "tracker")
@Service(value = "CEXio")
public class CexIoRetrievalService implements StockExrateRetrievalService {

   private static final Logger log = LogManager.getLogger("tracker");


    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;


    @Override
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        String jsonResponse = exchangeResponseProcessingService.sendGetRequest("https://cex.io/api/tickers/USD/BTC/EUR/");
        JsonNode root = exchangeResponseProcessingService.extractNode(jsonResponse, "data");
        return exchangeResponseProcessingService.extractAllStatsFromArrayNode(stockExchange, root, "pair",
                (name1, name2) -> name1.concat(":").concat(name2));

    }


}
