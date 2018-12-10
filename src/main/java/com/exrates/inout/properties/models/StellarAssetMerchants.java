package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "stellar-merchants")
public class StellarAssetMerchants {

    private StellarProperty slt;
    private StellarProperty tern;
    private StellarProperty vnt;
}
