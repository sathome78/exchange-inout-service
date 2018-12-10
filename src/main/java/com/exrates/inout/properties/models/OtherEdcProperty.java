package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherEdcProperty {

    private String token;
    private String mainAccount;
    private String hook;
    private String history;
    private String blockchainHostFast;
    private String blockchainHostDelayed;
    private String accountRegistrar;
    private String accountReferrer;
    private String accountMain;
    private String accountPrivateKey;
}
