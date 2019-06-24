package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monero-coins")
public class MoneroCoins {

    private MoneroProperty xmr;
    private MoneroProperty dit;
    private MoneroProperty sumo;
    private MoneroProperty hcxp;
}
