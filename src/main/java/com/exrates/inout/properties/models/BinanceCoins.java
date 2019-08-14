package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "binance-coins")
public class BinanceCoins {

    private BinanceProperty binance;

    private BinanceTokenProperty BNB;
    private BinanceTokenProperty ARN;
}
