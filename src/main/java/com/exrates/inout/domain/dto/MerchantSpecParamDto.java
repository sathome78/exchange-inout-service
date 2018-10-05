package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class MerchantSpecParamDto {

    private int id;
    private int merchantId;
    private String paramName;
    private String paramValue;
}
