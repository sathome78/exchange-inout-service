package com.exrates.inout.domain.dto.omni;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OmniWalletInfoDto {

    private BigDecimal balance;
    private BigDecimal reserved;
    private BigDecimal frozen;
}
