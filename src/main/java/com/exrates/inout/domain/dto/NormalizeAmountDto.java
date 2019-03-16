package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class NormalizeAmountDto {
    private Integer userId;
    private BigDecimal amount;
    private OperationType type;
    private Integer currencyId;
    private Integer merchantId;
    private String destinationTag;
    private UserRole userRole;
}
