package com.exrates.inout.exceptions;

/**
 * Created by maks on 20.06.2017.
 */
public class RequestsLimitExceedException extends RuntimeException {

    public RequestsLimitExceedException() {
    }

    public RequestsLimitExceedException(String message) {
        super(message);
    }
}
