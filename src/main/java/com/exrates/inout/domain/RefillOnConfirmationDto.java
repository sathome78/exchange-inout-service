package com.exrates.inout.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RefillOnConfirmationDto {

    private String hash;
    private BigDecimal amount;
    private String address;
    private Integer collectedConfirmations;
    private Integer neededConfirmations;
    @JsonIgnore
    private int merchantId;
}
