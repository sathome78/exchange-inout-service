package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "other-coins")
public class OtherCoins {

    private OtherQtumProperty qtum;
    private OtherAchainProperty achain;
    private OtherAdkProperty adk;
    private OtherApolloProperty apollo;
    private OtherEdcProperty edc;
    private OtherIotaProperty iota;
    private OtherDecredProperty decred;
    private OtherNemProperty nem;
    private OtherStellarProperty stellar;
    private OtherTronProperty tron;
    private OtherEdrProperty edr;
    private OtherUsdxProperty usdx;
    private OtherOmniProperty omni;
}
