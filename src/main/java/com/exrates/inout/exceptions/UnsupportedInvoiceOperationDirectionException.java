package com.exrates.inout.exceptions;

public class UnsupportedInvoiceOperationDirectionException extends RuntimeException {
    public UnsupportedInvoiceOperationDirectionException() {
    }

    public UnsupportedInvoiceOperationDirectionException(String message) {
        super(message);
    }

    public UnsupportedInvoiceOperationDirectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedInvoiceOperationDirectionException(Throwable cause) {
        super(cause);
    }
}
