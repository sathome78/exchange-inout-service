package com.exrates.inout.properties.models;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.neo.AssetMerchantCurrencyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class NeoProperty {

    private String merchantName;
    private String currencyName;
    private int minConfirmations;

    private String endpoint;
    private String address;

    private Merchant merchant;
    private Currency currency;
    private Map<String, AssetMerchantCurrencyDto> neoAssetMap;
}
