package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PerfectMoneyProperty {

    private String url;
    private String accountId;
    private String accountPass;
    private String payeeName;
    private String paymentSuccess;
    private String paymentFailure;
    private String paymentStatus;
    private String USDAccount;
    private String EURAccount;
    private String alternatePassphrase;
    private String ipWhiteList;
}
