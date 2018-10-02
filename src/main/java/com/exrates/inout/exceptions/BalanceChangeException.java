package com.exrates.inout.exceptions;

public class BalanceChangeException extends RuntimeException {
    public BalanceChangeException() { }

    public BalanceChangeException(String message) {
        super(message);
    }

}
