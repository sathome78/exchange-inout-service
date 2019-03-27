package com.exrates.inout.exceptions;

public class UnsupportedOperationTypeException extends RuntimeException {
    public UnsupportedOperationTypeException(int tupleId) {
        super("No such operation type " + tupleId);
    }

    public UnsupportedOperationTypeException(String s) {
        super(s);
    }
}