package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aisi-coins")
public class AisiCoins {
    private AisiProperty aisi;
}
