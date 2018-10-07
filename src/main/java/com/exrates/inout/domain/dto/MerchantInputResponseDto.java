package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.MerchantApiResponseType;
import lombok.Data;

@Data
public class MerchantInputResponseDto {
    private MerchantApiResponseType type;
    private String walletNumber;
    private Object data;
    private String qr;
    private String additionalTag;
}
