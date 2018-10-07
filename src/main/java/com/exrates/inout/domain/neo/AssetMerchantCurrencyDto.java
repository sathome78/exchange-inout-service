package com.exrates.inout.domain.neo;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class AssetMerchantCurrencyDto {
    NeoAsset asset;
    Merchant merchant;
    Currency currency;
}
