package com.exrates.inout.domain.enums;


import com.exrates.inout.exceptions.UnsupportedProcessTypeException;

import java.util.Arrays;

public enum CurrencyProcessType {
    FIAT, CRYPTO;

    public static CurrencyProcessType convert(String type) {
        return Arrays.stream(CurrencyProcessType.values())
                .filter(val -> val.name().equals(type))
                .findAny().orElseThrow(() -> new UnsupportedProcessTypeException(type));
    }

    @Override
    public String toString() {
        return this.name();
    }
}