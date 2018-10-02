package com.exrates.inout.exceptions;

import org.springframework.security.core.AuthenticationException;

public class NotVerifiedCaptchaError extends AuthenticationException {
    public NotVerifiedCaptchaError(String message) {
        super(message);
    }
}
