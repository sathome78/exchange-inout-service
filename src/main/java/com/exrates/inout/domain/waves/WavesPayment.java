package com.exrates.inout.domain.waves;

import lombok.Data;

@Data
public class WavesPayment {
    private String assetId;
    private String sender;
    private String recipient;
    private Long fee;
    private String feeAssetId;
    private Long amount;
    private String attachment = "";
}
