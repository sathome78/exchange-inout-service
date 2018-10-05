package com.exrates.inout.exceptions;

public class LiskRestException extends RuntimeException {
    public LiskRestException() {
    }

    public LiskRestException(String message) {
        super(message);
    }

    public LiskRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiskRestException(Throwable cause) {
        super(cause);
    }
}
