package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class YandexkassaProperty {

    private String shopId;
    private String scid;
    private String shopSuccessURL;
    private String paymentType;
    private String key;
    private String password;
}
