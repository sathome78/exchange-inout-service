package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherNemProperty {

    private String nccServerUrl;
    private String nisServerUrlReceive;
    private String nisServerUrlSend;
    private int transactionVersion;
    private String privateKey;
    private String publicKey;
    private String address;
}
