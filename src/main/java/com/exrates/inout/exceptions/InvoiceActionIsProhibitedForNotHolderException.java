package com.exrates.inout.exceptions;

public class InvoiceActionIsProhibitedForNotHolderException extends RuntimeException {
    public InvoiceActionIsProhibitedForNotHolderException(String message) {
        super(message);
    }
}
