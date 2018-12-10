package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Privat24Property {

    private String url;
    private String merchant;
    private String details;
    private String extDetails;
    private String payWay;
    private String returnUrl;
    private String serverUrl;
    private String password;
}
