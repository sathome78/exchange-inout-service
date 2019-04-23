package com.exrates.inout.service.stockExratesRetrieval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
import com.exrates.inout.util.BigDecimalProcessing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;
//exrates.model.util.BigDecimalProcessing;

/**
 * Created by OLEG on 20.12.2016.
 */
//@Log4j2(topic = "tracker")
@Service(value = "Gdax")
public class GdaxRetrievalService implements StockExrateRetrievalService {

   private static final Logger log = LogManager.getLogger("tracker");

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;


    @Override
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        List<StockExchangeStats> stockExchangeStatsList = new ArrayList<>();
        stockExchange.getAliasedCurrencyPairs((name1, name2) -> name1.concat("-").concat(name2))
                .forEach((currencyPairName, currencyPair) -> {
            String urlTicker = String.format("https://api.gdax.com/products/%s/ticker", currencyPairName);
            String urlStats = String.format("https://api.gdax.com/products/%s/stats", currencyPairName);
            String jsonResponseTicker = exchangeResponseProcessingService.sendGetRequest(urlTicker);
            String jsonResponseStats = exchangeResponseProcessingService.sendGetRequest(urlStats);
            try {
                JsonNode tickerRoot = objectMapper.readTree(jsonResponseTicker);
                JsonNode statsRoot = objectMapper.readTree(jsonResponseStats);
                StockExchangeStats stockExchangeStats = new StockExchangeStats();
                stockExchangeStats.setStockExchange(stockExchange);
                stockExchangeStats.setCurrencyPairId(currencyPair.getId());
                stockExchangeStats.setPriceSell(BigDecimalProcessing.parseNonePoint(tickerRoot.get("ask").asText()));
                stockExchangeStats.setPriceBuy(BigDecimalProcessing.parseNonePoint(tickerRoot.get("bid").asText()));
                stockExchangeStats.setPriceLast(BigDecimalProcessing.parseNonePoint(statsRoot.get("last").asText()));
                stockExchangeStats.setPriceLow(BigDecimalProcessing.parseNonePoint(statsRoot.get("low").asText()));
                stockExchangeStats.setPriceHigh(BigDecimalProcessing.parseNonePoint(statsRoot.get("high").asText()));
                stockExchangeStats.setVolume(BigDecimalProcessing.parseNonePoint(statsRoot.get("volume").asText()));
                stockExchangeStatsList.add(stockExchangeStats);
            } catch (IOException e) {
                //log.error(e);
            }
        });
        return stockExchangeStatsList;


    }

}
