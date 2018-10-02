package com.exrates.inout.exceptions;

public class NotEnoughUserWalletMoneyException extends RuntimeException {
    public NotEnoughUserWalletMoneyException(String message) {
        super(message);
    }
}