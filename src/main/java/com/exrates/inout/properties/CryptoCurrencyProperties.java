package com.exrates.inout.properties;

import com.exrates.inout.properties.models.BitcoinMerchants;
import com.exrates.inout.properties.models.EthereumMerchants;
import com.exrates.inout.properties.models.EthereumTokenMerchants;
import com.exrates.inout.properties.models.LiskMerchants;
import com.exrates.inout.properties.models.MoneroMerchants;
import com.exrates.inout.properties.models.NeoMerchants;
import com.exrates.inout.properties.models.QtumMerchants;
import com.exrates.inout.properties.models.WavesMerchants;
import com.exrates.inout.properties.models.XemMerchants;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@EnableConfigurationProperties({
        BitcoinMerchants.class,
        EthereumMerchants.class,
        EthereumTokenMerchants.class,
        LiskMerchants.class,
        WavesMerchants.class,
        NeoMerchants.class,
        QtumMerchants.class})
public class CryptoCurrencyProperties {

    private final BitcoinMerchants bitcoinMerchants;
    private final EthereumMerchants ethereumMerchants;
    private final EthereumTokenMerchants ethereumTokenMerchants;
    private final LiskMerchants liskMerchants;
    private final WavesMerchants wavesMerchants;
    private final NeoMerchants neoMerchants;
    private final QtumMerchants qtumMerchants;
    private final MoneroMerchants moneroMerchants;
    private final XemMerchants xemMerchants;

    @Autowired
    public CryptoCurrencyProperties(BitcoinMerchants bitcoinMerchants,
                                    EthereumMerchants ethereumMerchants,
                                    EthereumTokenMerchants ethereumTokenMerchants,
                                    LiskMerchants liskMerchants,
                                    WavesMerchants wavesMerchants,
                                    NeoMerchants neoMerchants,
                                    QtumMerchants qtumMerchants,
                                    MoneroMerchants moneroMerchants,
                                    XemMerchants xemMerchants) {
        this.bitcoinMerchants = bitcoinMerchants;
        this.ethereumMerchants = ethereumMerchants;
        this.ethereumTokenMerchants = ethereumTokenMerchants;
        this.liskMerchants = liskMerchants;
        this.wavesMerchants = wavesMerchants;
        this.neoMerchants = neoMerchants;
        this.qtumMerchants = qtumMerchants;
        this.moneroMerchants = moneroMerchants;
        this.xemMerchants = xemMerchants;
    }
}
