package com.exrates.inout.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ssm")
@Component
@Data
public class SsmProperties {
    private String mode;
    private String path;
}
