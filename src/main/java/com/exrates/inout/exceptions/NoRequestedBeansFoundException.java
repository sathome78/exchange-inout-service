package com.exrates.inout.exceptions;

public class NoRequestedBeansFoundException extends RuntimeException {
    public NoRequestedBeansFoundException() {
    }

    public NoRequestedBeansFoundException(String message) {
        super(message);
    }

    public NoRequestedBeansFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRequestedBeansFoundException(Throwable cause) {
        super(cause);
    }
}
