package com.exrates.inout.exceptions;

public class AbsentFinPasswordException extends MerchantException {

    private final String REASON_CODE = "admin.absentfinpassword";

    @Override
    public String getReason() {
        return REASON_CODE;
    }

}
