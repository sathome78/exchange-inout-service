package com.exrates.inout.service.stellar;

import com.exrates.inout.properties.models.StellarProperty;
import lombok.Data;
import org.stellar.sdk.Asset;
import org.stellar.sdk.KeyPair;

@Data
public class StellarAsset {

    private String currencyName;
    private String merchantName;
    private String assetName;
    private String emmitentAccount;
    private KeyPair issuer;
    private Asset asset;

    public StellarAsset(StellarProperty property) {
        this.currencyName = property.getCurrencyName();
        this.merchantName = property.getMerchantName();
        this.assetName = property.getAssetName();
        this.emmitentAccount = property.getEmmitentAccount();
        this.issuer = KeyPair.fromAccountId(property.getEmmitentAccount());
        this.asset = Asset.createNonNativeAsset(property.getAssetName(), this.issuer);
    }
}
