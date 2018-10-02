package com.exrates.inout.exceptions;

public class InvoiceActionIsProhibitedForCurrencyPermissionOperationException extends RuntimeException {
    public InvoiceActionIsProhibitedForCurrencyPermissionOperationException(String message) {
        super(message);
    }
}
