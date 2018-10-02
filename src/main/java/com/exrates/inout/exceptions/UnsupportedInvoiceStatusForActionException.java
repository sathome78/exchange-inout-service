package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class UnsupportedInvoiceStatusForActionException extends RuntimeException {
    public UnsupportedInvoiceStatusForActionException(String message) {
        super(message);
    }
}
