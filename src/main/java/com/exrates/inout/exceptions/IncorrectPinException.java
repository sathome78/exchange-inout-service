package com.exrates.inout.exceptions;

import com.exrates.inout.domain.dto.PinDto;
import org.springframework.security.core.AuthenticationException;

public class IncorrectPinException extends AuthenticationException {

    private final String REASON_CODE = "message.pin_code.incorrect";

    private PinDto dto;

    public IncorrectPinException(PinDto dto) {
        super(dto.getMessage());
        this.dto = dto;
    }

    public PinDto getDto() {
        return dto;
    }

    public String getReason() {
        return REASON_CODE;
    }
}
