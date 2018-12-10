package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LiqpayProperty {

    private String url;
    private String publicKey;
    private String privateKey;
    private int apiVersion;
    private String action;
}
