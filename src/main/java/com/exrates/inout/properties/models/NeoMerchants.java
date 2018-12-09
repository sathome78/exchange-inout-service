package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "neo-merchants")
public class NeoMerchants {

    private NeoProperty neo;
    private NeoProperty kaze;
}
