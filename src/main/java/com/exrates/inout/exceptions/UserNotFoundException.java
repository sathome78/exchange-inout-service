package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
