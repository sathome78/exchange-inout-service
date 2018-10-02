package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class TransferRequestNotFoundException extends RuntimeException {
    public TransferRequestNotFoundException(String message) {
        super(message);
    }
}
