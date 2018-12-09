package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class EthereumNode {

    private String currency;
    private String urlOurNode;
    private String url;
    private String destinationDir;
    private String password;
    private String mainAddress;
    private String transferAccAddress;
    private String transferAccPrivatKey;
    private String transferAccPublicKey;
    private BigDecimal minSumOnAccount;
    private BigDecimal minBalanceForTransfer;
    private String log;
}
