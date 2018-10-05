package com.exrates.inout.exceptions;

public class OrderParamsWrongException extends RuntimeException {
    public OrderParamsWrongException() {
        super();
    }

    public OrderParamsWrongException(String message) {
        super(message);
    }
}
