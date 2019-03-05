package com.exrates.inout.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "endpoints")
@Component
@Data
public class EndpointProperties {
    private String stock;
    private String inoutPrefix;

}
