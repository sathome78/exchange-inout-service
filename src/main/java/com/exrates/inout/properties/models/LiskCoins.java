package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "lisk-coins")
public class LiskCoins {

    private LiskProperty lisk;
    private LiskProperty btw;
    private LiskProperty rise;
    private ArkProperty ark;
}
