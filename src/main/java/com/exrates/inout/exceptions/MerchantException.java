package com.exrates.inout.exceptions;

public class MerchantException extends RuntimeException {
    private final String REASON_CODE = "withdraw.reject.reason.general";

    public MerchantException() {
    }

    public MerchantException(String message) {
        super(message);
    }

    public String getReason() {
        return REASON_CODE;
    }
}
