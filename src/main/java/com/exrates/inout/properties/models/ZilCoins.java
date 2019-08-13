package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "zil-coins")
public class ZilCoins {
    private ZilProperty zil;
}
