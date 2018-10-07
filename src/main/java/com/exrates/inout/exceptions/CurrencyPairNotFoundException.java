package com.exrates.inout.exceptions;

public class CurrencyPairNotFoundException extends RuntimeException {
    public CurrencyPairNotFoundException(String message) {
        super(message);
    }
}
