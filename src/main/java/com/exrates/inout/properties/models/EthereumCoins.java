package com.exrates.inout.properties.models;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ethereum-coins")
public class EthereumCoins {

    private EthereumProperty eth;
    private EthereumProperty etc;
    private EthereumProperty etz;
    private EthereumProperty clo;
    private EthereumProperty b2g;
    private EthereumProperty gol;
    private EthereumProperty cnet;
    private EthereumProperty nty;
    private EthereumProperty eti;
}
