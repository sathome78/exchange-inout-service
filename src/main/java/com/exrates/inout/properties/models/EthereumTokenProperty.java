package com.exrates.inout.properties.models;

import com.exrates.inout.service.ethereum.ExConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class EthereumTokenProperty {

    private String merchantName;
    private String currencyName;
    private String contract;
    private boolean isERC20;
    private ExConvert.Unit unit;
    private BigInteger minWalletBalance;
}
