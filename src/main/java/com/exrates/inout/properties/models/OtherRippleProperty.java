package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherRippleProperty {

    private String rippledRpcUrl;
    private String rippledWs;
    private String accountAddress;
    private String accountSecret;
}
