package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class InvoiceActionIsProhibitedForCurrentContextException extends RuntimeException {
    public InvoiceActionIsProhibitedForCurrentContextException(String message) {
        super(message);
    }
}
