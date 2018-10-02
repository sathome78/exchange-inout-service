package com.exrates.inout.exceptions.entity;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorInfo {
    private final String url;
    private final String cause;
    private final String detail;

    public ErrorInfo(CharSequence url, Throwable ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        String detail = ex.getLocalizedMessage() == null ? ex.getLocalizedMessage() : ex.getMessage();
        while (ex.getCause() != null) ex = ex.getCause();
        this.detail = ex.getLocalizedMessage() == null ? detail : ex.getLocalizedMessage();
    }

    public ErrorInfo(CharSequence url, Throwable ex, String reason) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        while (ex.getCause() != null) ex = ex.getCause();
        this.detail = reason;
    }

    public ErrorInfo(CharSequence url, MethodArgumentNotValidException ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (!errors.isEmpty()) {
            this.detail = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(";\n"));
        } else {
            this.detail = ex.getLocalizedMessage();
        }

    }

    public ErrorInfo(CharSequence url, BindException ex) {
        this.url = url.toString();
        this.cause = ex.getClass().getSimpleName();

        List<ObjectError> errors = ex.getAllErrors();
        if (!errors.isEmpty()) {
            this.detail = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(";\n"));
            System.out.println(detail);
        } else {
            this.detail = ex.getLocalizedMessage();
        }

    }

}
