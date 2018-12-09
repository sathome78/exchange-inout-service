package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class XemProperty {

    private String merchantName;
    private String currencyName;
    private String nameSpaceId;
    private String name;
    private int decimals;
    private int divisibility;
    private int levyFee;
    private long supply;
}
