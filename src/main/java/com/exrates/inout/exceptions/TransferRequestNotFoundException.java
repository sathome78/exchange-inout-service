package com.exrates.inout.exceptions;

public class TransferRequestNotFoundException extends RuntimeException {
    public TransferRequestNotFoundException(String message) {
        super(message);
    }
}
