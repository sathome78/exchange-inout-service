package com.exrates.inout.exceptions;

public class NotConfirmedFinPasswordException extends MerchantException {

    private final String REASON_CODE = "admin.notconfirmedfinpassword";

    public String getReason() {
        return REASON_CODE;
    }

}
