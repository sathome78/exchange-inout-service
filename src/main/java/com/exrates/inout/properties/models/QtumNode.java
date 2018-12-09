package com.exrates.inout.properties.models;

import com.exrates.inout.util.ExConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class QtumNode {

    private String contract;
    private ExConvert.Unit unit;
    private String endpoint;
    private String user;
    private String password;
    private int minTransferAmount;
    private String mainAddressForTransfer;
    private String walletPassword;
    private String backupFolder;
}
