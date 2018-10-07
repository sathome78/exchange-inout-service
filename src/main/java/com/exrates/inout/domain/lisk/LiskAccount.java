package com.exrates.inout.domain.lisk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiskAccount {
    private String address;
    private BigDecimal unconfirmedBalance = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private String publicKey;
}
