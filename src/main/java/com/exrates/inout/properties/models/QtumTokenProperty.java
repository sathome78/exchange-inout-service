package com.exrates.inout.properties.models;

import com.exrates.inout.service.ethereum.ExConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class QtumTokenProperty {

    private String merchantName;
    private String currencyName;
    private String contract;
    private ExConvert.Unit unit;
}
