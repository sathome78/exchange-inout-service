package com.exrates.inout.service.stockExratesRetrieval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
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
@Service(value = "alcurEX")
public class AlcurExRetrievalService implements StockExrateRetrievalService {

   private static final Logger log = LogManager.getLogger("tracker");


    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;

    @Override
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        String jsonResponse = exchangeResponseProcessingService.sendGetRequest("https://alcurex.com/api/tickerapi");
        return exchangeResponseProcessingService.extractAllStatsFromMapNode(stockExchange, jsonResponse, (name1, name2) -> name1.concat("_").concat(name2));
    }


}
