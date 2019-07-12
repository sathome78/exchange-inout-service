package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MoneroProperty {

    private String merchantName;
    private String currencyName;
    private int minConfirmations;
    private int decimals;
    private String mainAccount;

    private MoneroNode node;
}
