package com.exrates.inout.exceptions;

public class InvalidAccountException extends MerchantException {
    private final String REASON_CODE = "withdraw.reject.reason.invalidAccount";

    public InvalidAccountException(){}

    public String getReason() {
        return REASON_CODE;
    }
}
