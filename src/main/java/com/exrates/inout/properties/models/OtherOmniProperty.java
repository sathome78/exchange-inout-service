package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherOmniProperty {

    private String rpcHost;
    private String rpcUser;
    private String rpcPassword;
    private String hotWalletAddress;
    private String commissionAddress;
}
