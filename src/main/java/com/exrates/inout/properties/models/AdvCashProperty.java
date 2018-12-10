package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AdvCashProperty {

    private String url;
    private String accountId;
    private String payeeName;
    private String paymentSuccess;
    private String paymentFailure;
    private String paymentStatus;
    private String USDAccount;
    private String EURAccount;
    private String payeePassword;
    private String ipWhiteList;
}
