package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.OrderType;
import com.exrates.inout.util.BigDecimalToDoubleSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderBookItem {

    @JsonIgnore
    private OrderType orderType;

    @JsonSerialize(using = BigDecimalToDoubleSerializer.class)
    private BigDecimal amount;

    @JsonSerialize(using = BigDecimalToDoubleSerializer.class)
    private BigDecimal rate;
}
