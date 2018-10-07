package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@NoArgsConstructor
@Getter
@Setter
public class OrderCreationParamsDto {
    @NotNull
    private Integer currencyPairId;
    @NotNull
    private OperationType orderType;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private BigDecimal rate;

    public OrderCreationParamsDto(Integer currencyPairId, OperationType orderType, BigDecimal amount, BigDecimal rate) {
        this.currencyPairId = currencyPairId;
        this.orderType = orderType;
        this.amount = amount;
        this.rate = rate;
    }

}
