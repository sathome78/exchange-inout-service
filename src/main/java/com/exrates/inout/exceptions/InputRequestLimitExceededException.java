package com.exrates.inout.exceptions;

public class InputRequestLimitExceededException extends RuntimeException {

    public InputRequestLimitExceededException(String message) {
        super(message);
    }

}
