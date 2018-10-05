package com.exrates.inout.exceptions;

public class InvalidAccountException extends MerchantException {
    private final String REASON_CODE = "withdraw.reject.reason.invalidAccount";

    public InvalidAccountException() {
    }

    public InvalidAccountException(WavesRestException e) {
        super(e);
    }

    public InvalidAccountException(String reason) {
        super(reason);
    }

    public String getReason() {
        return REASON_CODE;
    }
}
