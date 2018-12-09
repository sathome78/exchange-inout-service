package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LiskProperty {

    private String merchantName;
    private String currencyName;
    private int minConfirmations;
    private String createAccountEndpoint;
    private String createTxEndpoint;
    private String broadcastTxEndpoint;

    private LiskNode node;
}
