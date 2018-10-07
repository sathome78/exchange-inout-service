package com.exrates.inout.domain.dto;

import com.exrates.inout.util.BigDecimalToDoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBalanceDto {

    private String currencyName;
    @JsonSerialize(using = BigDecimalToDoubleSerializer.class)
    private BigDecimal activeBalance;
    @JsonSerialize(using = BigDecimalToDoubleSerializer.class)
    private BigDecimal reservedBalance;

}
