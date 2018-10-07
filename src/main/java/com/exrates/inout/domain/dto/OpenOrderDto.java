package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OpenOrderDto {

    private Integer id;
    @JsonProperty("order_type")
    private String orderType;
    private BigDecimal amount;
    private BigDecimal price;

}
