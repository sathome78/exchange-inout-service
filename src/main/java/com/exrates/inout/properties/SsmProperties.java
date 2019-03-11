package com.exrates.inout.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "ssm")
@Component
@Data
@NoArgsConstructor
public class SsmProperties {
    private String mode;
    private String path;
}
