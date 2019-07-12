package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "xem-coins")
public class XemCoins {

    private XemProperty dim;
    private XemProperty npxs;
    private XemProperty DIM_EUR;
    private XemProperty DIM_USD;
    private XemProperty DIGIC;
    private XemProperty DARC;
    private XemProperty RWDS;
}
