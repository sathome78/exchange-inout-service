package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherDecredProperty {

    private String rpcUser;
    private String rpcPassword;
    private String serverWsUrl;
    private String host;
    private int port;
    private String certPath;
}
