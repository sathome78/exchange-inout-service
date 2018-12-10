package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "waves-coins")
public class WavesCoins {

    private WavesProperty waves;
    private WavesProperty lunes;
}
