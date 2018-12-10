package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NixMoneyProperty {

    private String url;
    private String payeeAccountUSD;
    private String payeeAccountEUR;
    private String payeePassword;
    private String payeeName;
    private String paymentUrl;
    private String noPaymentUrl;
    private String statustUrl;
}
