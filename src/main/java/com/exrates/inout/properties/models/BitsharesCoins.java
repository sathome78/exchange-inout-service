package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "bitshares-coins")
public class BitsharesCoins {
    private BitsharesProperty aunit;
    private BitsharesProperty crea;
    private BitsharesProperty ppy;
}
