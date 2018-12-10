package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OkPayProperty {

    private String receiver;
    private String receiverEmail;
    private String itemName;
    private String sTitle;
    private String url;
    private String urlReturn;
}
