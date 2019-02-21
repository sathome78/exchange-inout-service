package com.exrates.inout.domain.main;

import com.exrates.inout.util.BigDecimalToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MerchantCurrency {
    private int merchantId;
    private int currencyId;
    private String name;
    private String description;
    @JsonSerialize(using = BigDecimalToStringSerializer.class)
    private BigDecimal minSum;
    @JsonSerialize(using = BigDecimalToStringSerializer.class)
    private BigDecimal inputCommission;
    @JsonSerialize(using = BigDecimalToStringSerializer.class)
    private BigDecimal outputCommission;
    @JsonSerialize(using = BigDecimalToStringSerializer.class)
    private BigDecimal fixedMinCommission;
    private List<MerchantImage> listMerchantImage;
    private String processType;
    private String mainAddress;
    private String address;
    private Boolean additionalTagForWithdrawAddressIsUsed;
    private Boolean additionalTagForRefillIsUsed;
    private String additionalFieldName;
    private Boolean generateAdditionalRefillAddressAvailable;
    private Boolean recipientUserIsNeeded;
    private Boolean comissionDependsOnDestinationTag;
    private Boolean specMerchantComission;
    private Boolean availableForRefill;
}