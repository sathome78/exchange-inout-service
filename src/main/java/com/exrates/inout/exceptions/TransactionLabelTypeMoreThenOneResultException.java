package com.exrates.inout.exceptions;

public class TransactionLabelTypeMoreThenOneResultException extends RuntimeException {
    public TransactionLabelTypeMoreThenOneResultException(String message) {
        super(message);
    }
}
