package com.exrates.inout.domain.enums;


import com.exrates.inout.exceptions.UnsupportedProcessTypeException;

import java.util.Arrays;
import java.util.List;

public enum MerchantProcessType {
    MERCHANT, CRYPTO, INVOICE, TRANSFER;

    public static MerchantProcessType convert(String type) {
        return Arrays.stream(MerchantProcessType.values())
                .filter(val -> val.name().equals(type))
                .findAny().orElseThrow(() -> new UnsupportedProcessTypeException(type));
    }

    @Override
    public String toString() {
        return this.name();
    }

    public static List<MerchantProcessType> getAllCoinsTypes() {
        return Arrays.asList(MerchantProcessType.MERCHANT, MerchantProcessType.INVOICE, MerchantProcessType.CRYPTO);
    }

    public static CurrencyProcessType toCurrencyProcessType(MerchantProcessType type) {
        switch (type) {
            case CRYPTO:
                return CurrencyProcessType.CRYPTO;
            case MERCHANT:
            case INVOICE:
                return CurrencyProcessType.FIAT;
            default:
                throw new UnsupportedProcessTypeException(type.toString());
        }
    }
}