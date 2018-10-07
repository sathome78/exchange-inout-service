package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.MerchantCurrency;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CryptoAddressDto {

    private Integer merchantId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String mainAddress;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalFieldName;

    public CryptoAddressDto(MerchantCurrency merchantCurrency) {
        merchantId = merchantCurrency.getMerchantId();
        mainAddress = merchantCurrency.getMainAddress();
        address = merchantCurrency.getAddress();
        additionalFieldName = merchantCurrency.getAdditionalFieldName();
    }
}
