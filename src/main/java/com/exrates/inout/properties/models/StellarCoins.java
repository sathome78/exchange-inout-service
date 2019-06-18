package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "stellar-coins")
public class StellarCoins {

    private StellarAssetProperty slt;
    private StellarAssetProperty tern;
    private StellarAssetProperty vnt;
}
