package com.exrates.inout.service.stockExratesRetrieval;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.StockExchange;
import com.exrates.inout.domain.dto.StockExchangeStats;
import com.exrates.inout.domain.main.CurrencyPair;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//exrates.model.CurrencyPair;
//exrates.model.StockExchange;
//exrates.model.StockExchangeStats;

/**
 * Created by OLEG on 15.12.2016.
 */
//@Log4j2(topic = "tracker")
@Service(value = "xBTCe")
public class XBTCeRetrievalService implements StockExrateRetrievalService {

   private static final Logger log = LogManager.getLogger("tracker");


    @Autowired
    private ExchangeResponseProcessingService exchangeResponseProcessingService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<StockExchangeStats> retrieveStats(StockExchange stockExchange) {
        Map<String, CurrencyPair> currencyPairs = stockExchange.getAliasedCurrencyPairs(String::concat);
        String urlBase = "https://cryptottlivewebapi.xbtce.net:8443/api/v1/public/ticker/";
        String urlFilter = currencyPairs.keySet().stream().collect(Collectors.joining(" "));
        String jsonResponse = exchangeResponseProcessingService.sendGetRequest(urlBase + urlFilter);

        JsonNode root = exchangeResponseProcessingService.extractNode(jsonResponse);

        List<StockExchangeStats> stockExchangeStatsList = new ArrayList<>();
        root.elements().forEachRemaining(jsonNode -> {
            CurrencyPair currencyPair = currencyPairs.get(root.get("Symbol").asText());
            if (currencyPair != null) {
                stockExchange.setLastFieldName(extractLastPriceField(jsonNode));
                StockExchangeStats stockExchangeStats = stockExchange.extractStatsFromNode(jsonNode, currencyPair.getId());
                stockExchangeStatsList.add(stockExchangeStats);
            }

        });
        return stockExchangeStatsList;


    }

    private String extractLastPriceField(JsonNode jsonNode) {
        long lastBuyTimestamp = jsonNode.get("LastBuyTimestamp").longValue();
        long lastSellTimestamp = jsonNode.get("LastSellTimestamp").longValue();
        return lastBuyTimestamp > lastSellTimestamp ? "LastBuyPrice" : "LastSellPrice";
    }


}
