package com.exrates.inout.exceptions;

public class MerchantInternalException extends RuntimeException {

    public MerchantInternalException() {
    }

    public MerchantInternalException(Exception runtimeException) {
        super(runtimeException);
    }


    public MerchantInternalException(String name) {
        super(name);
    }
}