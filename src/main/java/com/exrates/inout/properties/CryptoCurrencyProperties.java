package com.exrates.inout.properties;

import com.exrates.inout.properties.models.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@EnableConfigurationProperties({
        BitcoinCoins.class,
        EthereumCoins.class,
        EthereumTokenCoins.class,
        LiskCoins.class,
        WavesCoins.class,
        NeoCoins.class,
        QtumCoins.class,
        MoneroCoins.class,
        XemCoins.class,
        StellarCoins.class,
        OtherCoins.class,
        PaymentSystemMerchants.class,
        NemCoins.class,
        Gapicoin.class,
        Aisicoin.class,
        AwsProperty.class})
public class CryptoCurrencyProperties {

    private final BitcoinCoins bitcoinCoins;
    private final EthereumCoins ethereumCoins;
    private final EthereumTokenCoins ethereumTokenCoins;
    private final LiskCoins liskCoins;
    private final WavesCoins wavesCoins;
    private final NeoCoins neoCoins;
    private final QtumCoins qtumCoins;
    private final MoneroCoins moneroCoins;
    private final XemCoins xemCoins;
    private final StellarCoins stellarCoins;
    private final OtherCoins otherCoins;
    private final PaymentSystemMerchants paymentSystemMerchants;
    private final NemCoins nemCoins;
    private final Gapicoin gapicoin;
    private final Aisicoin aisicoin;
    private final AwsProperty awsProperty;

    @Autowired
    public CryptoCurrencyProperties(BitcoinCoins bitcoinCoins,
                                    EthereumCoins ethereumCoins,
                                    EthereumTokenCoins ethereumTokenCoins,
                                    LiskCoins liskCoins,
                                    WavesCoins wavesCoins,
                                    NeoCoins neoCoins,
                                    QtumCoins qtumCoins,
                                    MoneroCoins moneroCoins,
                                    XemCoins xemCoins,
                                    StellarCoins stellarCoins,
                                    OtherCoins otherCoins,
                                    PaymentSystemMerchants paymentSystemMerchants,
                                    NemCoins nemCoins, Gapicoin gapicoin, Aisicoin aisicoin, AwsProperty awsProperty) {

        this.bitcoinCoins = bitcoinCoins;
        this.ethereumCoins = ethereumCoins;
        this.ethereumTokenCoins = ethereumTokenCoins;
        this.liskCoins = liskCoins;
        this.wavesCoins = wavesCoins;
        this.neoCoins = neoCoins;
        this.qtumCoins = qtumCoins;
        this.moneroCoins = moneroCoins;
        this.xemCoins = xemCoins;
        this.stellarCoins = stellarCoins;
        this.otherCoins = otherCoins;
        this.paymentSystemMerchants = paymentSystemMerchants;
        this.nemCoins = nemCoins;
        this.gapicoin = gapicoin;
        this.aisicoin = aisicoin;
        this.awsProperty = awsProperty;
    }
}


