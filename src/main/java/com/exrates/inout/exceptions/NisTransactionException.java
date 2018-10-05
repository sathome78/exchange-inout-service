package com.exrates.inout.exceptions;

public class NisTransactionException extends RuntimeException {

    public NisTransactionException() {
    }

    public NisTransactionException(String message) {
        super(message);
    }
}
