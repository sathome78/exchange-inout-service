package com.exrates.inout.exceptions;

public class InvoiceActionIsProhibitedForCurrentContextException extends RuntimeException {
    public InvoiceActionIsProhibitedForCurrentContextException(String message) {
        super(message);
    }
}
