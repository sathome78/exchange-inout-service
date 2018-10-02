package com.exrates.inout.exceptions;

public class RefillRequestAlreadyAcceptedException extends RuntimeException {
    public RefillRequestAlreadyAcceptedException(String message) {
        super(message);
    }
}
