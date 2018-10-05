package com.exrates.inout.exceptions;

public class CoreWalletPasswordNotFoundException extends RuntimeException {
    public CoreWalletPasswordNotFoundException() {
    }

    public CoreWalletPasswordNotFoundException(String message) {
        super(message);
    }
}
