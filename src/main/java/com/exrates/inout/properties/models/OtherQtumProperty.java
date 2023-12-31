package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherQtumProperty {

    private String endpoint;
    private String user;
    private String password;
    private int minConfirmations;
    private BigDecimal minTransferAmount;
    private String mainAddressForTransfer;
    private String walletPassword;
    private String backupFolder;
}
