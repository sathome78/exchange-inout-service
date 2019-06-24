package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class InterkassaProperty {

    private String url;
    private String checkoutId;
    private String statusUrl;
    private String successUrl;
    private String secretKey;
    private String secretUrl;
    private String username;
    private String password;
}
