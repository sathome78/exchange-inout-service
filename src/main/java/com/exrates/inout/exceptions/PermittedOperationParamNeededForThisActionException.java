package com.exrates.inout.exceptions;

public class PermittedOperationParamNeededForThisActionException extends RuntimeException {
    public PermittedOperationParamNeededForThisActionException(String message) {
        super(message);
    }
}
