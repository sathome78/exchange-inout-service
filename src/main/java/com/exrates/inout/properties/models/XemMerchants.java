package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "xem-merchants")
public class XemMerchants {

    private XemProperty dim;
    private XemProperty npxs;
}
