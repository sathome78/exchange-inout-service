package com.exrates.inout.exceptions;

public class IncorrectCoreWalletPasswordException extends RuntimeException {
    public IncorrectCoreWalletPasswordException() {
    }

    public IncorrectCoreWalletPasswordException(String message) {
        super(message);
    }
}
