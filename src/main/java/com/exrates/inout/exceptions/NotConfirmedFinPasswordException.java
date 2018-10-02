package com.exrates.inout.exceptions;

/**
 * Created by Valk on 04.04.16.
 */
public class NotConfirmedFinPasswordException extends MerchantException {

    private final String REASON_CODE = "admin.notconfirmedfinpassword";

    @Override
    public String getReason() {
        return REASON_CODE;
    }

    public NotConfirmedFinPasswordException() {
    }

    public NotConfirmedFinPasswordException(String message) {
        super(message);
    }
}
