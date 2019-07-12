package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArkNode extends LiskNode {
    private String arkCreateAccountEndpoint;
    private String arkCreateTxEndpoint;
    private String arkBroadcastTxEndpoint;

}
