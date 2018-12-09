package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
public class WavesNode {

    private String host;
    private String port;
    private String apiKey;
    private String mainAccount;
    private int processDelay;
    private String feeAccount;
    private String notifyEmail;
    private Map<String, String> tokenIds;
}
