package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "monero-merchants")
public class MoneroMerchants {

    private MoneroProperty xmr;
    private MoneroProperty dit;
    private MoneroProperty sumo;
}
