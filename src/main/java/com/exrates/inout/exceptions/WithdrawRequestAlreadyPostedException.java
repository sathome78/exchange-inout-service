package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class WithdrawRequestAlreadyPostedException extends RuntimeException {
    public WithdrawRequestAlreadyPostedException(String message) {
        super(message);
    }
}
