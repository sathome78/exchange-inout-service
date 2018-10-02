package com.exrates.inout.domain.main;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MerchantCurrency {
    private int merchantId;
    private int currencyId;
    private String name;
    private String description;
    private BigDecimal minSum;
    private BigDecimal inputCommission;
    private BigDecimal outputCommission;
    private BigDecimal fixedMinCommission;
    private List<MerchantImage> listMerchantImage;
    private String processType;
    private String mainAddress;
    private String address;
    private Boolean additionalTagForWithdrawAddressIsUsed;
    private String additionalFieldName;
    private Boolean generateAdditionalRefillAddressAvailable;
    private Boolean recipientUserIsNeeded;
    private Boolean comissionDependsOnDestinationTag;
    private Boolean specMerchantComission;
}