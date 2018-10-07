package com.exrates.inout.exceptions;

public class UnsupportedTransactionStatusException extends RuntimeException {
    public UnsupportedTransactionStatusException(int transactionStatusId) {
        super("No such transaction status " + transactionStatusId);
    }
}