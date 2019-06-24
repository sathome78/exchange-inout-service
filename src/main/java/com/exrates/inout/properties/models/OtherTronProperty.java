package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherTronProperty {

    private String fullNodeUrl;
    private String fullNodeForSendUrl;
    private String solidityNodeUrl;
    private String explorerApi;
    private String mainAccountAddress;
    private String mainAccountHexAddress;
}
