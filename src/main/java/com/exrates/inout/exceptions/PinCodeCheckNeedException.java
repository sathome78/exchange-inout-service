package com.exrates.inout.exceptions;

import org.springframework.security.core.AuthenticationException;

public class PinCodeCheckNeedException extends AuthenticationException {
    public PinCodeCheckNeedException(String msg) {
        super(msg);
    }
}
