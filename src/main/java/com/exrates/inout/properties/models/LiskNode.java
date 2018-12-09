package com.exrates.inout.properties.models;

import com.fasterxml.jackson.databind.node.JsonNodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class LiskNode {

    private String host;
    private String port;
    private String address;
    private String secret;
    private String txSortPrefix;
    private int txQueryLimit;
    private JsonNodeType txCountNodeType;
    private String portGetAccount;
    private String portSendTx;
}
