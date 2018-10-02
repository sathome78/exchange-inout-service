package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class MerchantNotFoundException extends RuntimeException {
    public MerchantNotFoundException(String message) {
        super(message);
    }
}
