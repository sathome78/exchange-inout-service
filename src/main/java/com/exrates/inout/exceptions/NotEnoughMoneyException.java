package com.exrates.inout.exceptions;

/**
 * Created by Valk on 04.04.16.
 */
public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
