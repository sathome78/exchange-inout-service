package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder(toBuilder = true)
@Data
public class RippleAccount {

    private String name;
    private String secret;

    @Tolerate
    public RippleAccount() {
    }
}
