package com.exrates.inout.exceptions;

import lombok.Data;

@Data
public class ForceGenerationAddressException extends Throwable {
    private String address;
    private String message;

    public ForceGenerationAddressException(String address, String message) {
        this.address = address;
        this.message = message;
    }
}
