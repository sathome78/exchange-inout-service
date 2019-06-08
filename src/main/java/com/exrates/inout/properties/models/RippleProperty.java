package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RippleProperty {

    private String rpcUrl;
    private String ws;
    private String accountAddress;
    private String accountSecret;
}
