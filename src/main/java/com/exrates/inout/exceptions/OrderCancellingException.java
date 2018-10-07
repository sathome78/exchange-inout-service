package com.exrates.inout.exceptions;

public class OrderCancellingException extends RuntimeException {
    public OrderCancellingException(String message) {
        super(message);
    }
}
