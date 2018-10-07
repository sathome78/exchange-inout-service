package com.exrates.inout.exceptions;

public class TransactionPersistException extends RuntimeException {
    public TransactionPersistException(final String message) {
        super(message);
    }
}