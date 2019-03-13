package com.exrates.inout.exceptions;

public class CheckDestinationTagException extends Exception {

    private String fieldName;

    public CheckDestinationTagException(String message, String fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
