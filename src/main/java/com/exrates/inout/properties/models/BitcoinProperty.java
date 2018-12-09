package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BitcoinProperty {

    private String merchantName;
    private String currencyName;
    private int minConfirmations;
    private int blockTargetForFee;
    private boolean rawTxEnabled;
    private boolean supportSubtractFee;
    private boolean supportWalletNotifications;
    private boolean supportReferenceLine;

    private String backupFolder;
    private String walletPassword;

    private BitcoinNode node;
}
