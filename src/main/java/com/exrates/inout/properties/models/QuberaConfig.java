package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "qubera")
public class QuberaConfig {

    private String kycUrl;
    private String kycApiKey;
    private int thresholdLength;
    private int poolId;
    private String masterAccount;
}
