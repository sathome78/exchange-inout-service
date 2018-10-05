package com.exrates.inout.exceptions;

public class RippleCheckConsensusException extends RuntimeException {

    public RippleCheckConsensusException() {
    }

    public RippleCheckConsensusException(String message) {
        super(message);
    }

    public RippleCheckConsensusException(String message, Throwable cause) {
        super(message, cause);
    }

    public RippleCheckConsensusException(Throwable cause) {
        super(cause);
    }

    public RippleCheckConsensusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
