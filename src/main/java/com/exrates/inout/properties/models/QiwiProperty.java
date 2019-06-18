package com.exrates.inout.properties.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QiwiProperty {
    private String clientId;
    private String clientSecret;
    private String productionUrl;
    private String developmentUrl;
    private String accountAddress;
    private String accountPin;
    private int transactionStartedPosition;
    private int transactionLimit;
}
