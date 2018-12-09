package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "qtum-merchants")
public class QtumMerchants {

    QtumProperty spc;
    QtumProperty hlc;
}
