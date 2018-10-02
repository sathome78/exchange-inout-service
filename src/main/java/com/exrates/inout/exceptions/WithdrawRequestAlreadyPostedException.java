package com.exrates.inout.exceptions;

public class WithdrawRequestAlreadyPostedException extends RuntimeException {
    public WithdrawRequestAlreadyPostedException(String message) {
        super(message);
    }
}
