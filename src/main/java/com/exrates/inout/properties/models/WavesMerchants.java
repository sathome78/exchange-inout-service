package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "waves-merchants")
public class WavesMerchants {

    private WavesProperty waves;
    private WavesProperty lunes;
}
