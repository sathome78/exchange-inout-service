package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qtum-coins")
public class QtumCoins {

    QtumTokenProperty spc;
    QtumTokenProperty hlc;
}
