package com.exrates.inout.exceptions;

public class UnsupportedInvoiceStatusForActionException extends RuntimeException {
    public UnsupportedInvoiceStatusForActionException(String message) {
        super(message);
    }
}
