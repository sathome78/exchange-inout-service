package com.exrates.inout.exceptions;

public class CommissionExceedingAmountException extends RuntimeException {

    public CommissionExceedingAmountException(String message) {
        super(message);
    }

}
