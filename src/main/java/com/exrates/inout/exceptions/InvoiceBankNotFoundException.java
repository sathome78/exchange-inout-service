package com.exrates.inout.exceptions;

/**
 * Created by OLEG on 30.05.2017.
 */
public class InvoiceBankNotFoundException extends RuntimeException {
    public InvoiceBankNotFoundException() {
    }

    public InvoiceBankNotFoundException(String message) {
        super(message);
    }
}
