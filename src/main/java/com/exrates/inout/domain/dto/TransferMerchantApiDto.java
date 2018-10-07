package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class TransferMerchantApiDto {
    private Integer merchantId;
    private String name;
    private Boolean isVoucher;
    private Boolean recipientUserIsNeeded;
    private List<Integer> blockedForCurrencies;

    @JsonIgnore
    private String serviceBeanName;
}
