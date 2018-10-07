package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommissionsDto {

    private BigDecimal inputCommission;
    private BigDecimal outputCommission;
    private BigDecimal sellCommission;
    private BigDecimal buyCommission;
    private BigDecimal transferCommission;

}
