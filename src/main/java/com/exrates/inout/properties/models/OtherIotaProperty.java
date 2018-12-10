package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OtherIotaProperty {

    private String protocol;
    private String host;
    private int port;
    private String seed;
    private String message;
    private String tag;
    private String mode;
}
