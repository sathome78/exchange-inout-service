package com.exrates.inout.exceptions;

public class QtumApiException extends RuntimeException {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public QtumApiException(String message) {
        super(message);
    }

    public QtumApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public QtumApiException(Throwable cause) {
        super(cause);
    }
}
