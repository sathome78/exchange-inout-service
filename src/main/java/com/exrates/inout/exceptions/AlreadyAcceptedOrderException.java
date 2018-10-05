package com.exrates.inout.exceptions;

public class AlreadyAcceptedOrderException extends OrderAcceptionException {
    public AlreadyAcceptedOrderException(String message) {
        super(message);
    }
}
