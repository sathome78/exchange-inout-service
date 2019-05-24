package com.exrates.inout.properties.models;

import lombok.Data;

@Data
public class NemProperty {
    private String nccServerUrl;
    private String nisServerUrlReceive;
    private String nisServerUrlSend;
    private String privateKey;
    private String publicKey;
    private String address;
    private int transactionVersion;
}
