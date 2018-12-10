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
    private OtherAunitProperty aunit;
    private OtherEdcProperty edc;
    private OtherIotaProperty iota;
    private OtherDecredProperty decred;
    private OtherNemProperty nem;
    private OtherRippleProperty ripple;
    private OtherStellarProperty stellar;
    private OtherTronProperty tron;
    private OtherEdrProperty edr;
}
