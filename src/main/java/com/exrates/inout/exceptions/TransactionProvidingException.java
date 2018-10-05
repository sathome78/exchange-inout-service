package com.exrates.inout.exceptions;

public class TransactionProvidingException extends RuntimeException {
    public TransactionProvidingException(String message) {
        super(message);
    }
}