package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BinanceProperty {

    private String binanceMainAddress;
    private String binanceNodeHost;
    private String binanceNodePort;
    private String merchantName;
    private String confirmations;
}
