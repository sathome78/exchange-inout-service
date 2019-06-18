package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ripple-coins")
public class RippleCoins {

    private RippleProperty ripple;
    private RippleProperty csc;

}
