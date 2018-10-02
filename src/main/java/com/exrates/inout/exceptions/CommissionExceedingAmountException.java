package com.exrates.inout.exceptions;

public class CommissionExceedingAmountException extends RuntimeException {
    public CommissionExceedingAmountException() {
    }

    public CommissionExceedingAmountException(String message) {
        super(message);
    }

    public CommissionExceedingAmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommissionExceedingAmountException(Throwable cause) {
        super(cause);
    }
}
