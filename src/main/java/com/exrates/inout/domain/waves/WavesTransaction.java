package com.exrates.inout.domain.waves;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WavesTransaction {


    private Integer type;
    private String id;
    private String sender;
    private String senderPublicKey;
    private String assetId;
    private Long fee;
    private Long timestamp;
    private String signature;
    private String recipient;
    private Long amount;
    private String attachment;
    private Integer height;

}
