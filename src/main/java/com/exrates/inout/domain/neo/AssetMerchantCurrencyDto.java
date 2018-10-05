package com.exrates.inout.domain.neo;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class AssetMerchantCurrencyDto {
    NeoAsset asset;
    Merchant merchant;
    Currency currency;
}
