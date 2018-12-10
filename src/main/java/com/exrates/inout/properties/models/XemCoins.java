package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "xem-coins")
public class XemCoins {

    private XemProperty dim;
    private XemProperty npxs;
}
