package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class YandexMoneyProperty {

    private String clientId;
    private String token;
    private String redirectURI;
    private String apiRedirectURI;
    private String responseType;
    private String mediaType;
    private String companyWalletId;
}
