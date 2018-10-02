package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MerchantCurrencyApiDto {
    private Integer merchantId;
    private Integer currencyId;
    private String name;
    private String processType;
    private BigDecimal minInputSum;
    private BigDecimal minOutputSum;
    private BigDecimal minTransferSum;
    private BigDecimal inputCommission;
    private BigDecimal outputCommission;
    private BigDecimal transferCommission;
    private BigDecimal minFixedCommission;
    @JsonProperty(value = "withdrawBlocked")
    private Boolean isWithdrawBlocked;
    @JsonProperty(value = "refillBlocked")
    private Boolean isRefillBlocked;
    @JsonProperty(value = "transferBlocked")
    private Boolean isTransferBlocked;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String additionalFieldName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean generateAdditionalRefillAddressAvailable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean withdrawCommissionDependsOnDestinationTag;
    private List<MerchantImageShortenedDto> listMerchantImage;
    @JsonIgnore
    private String serviceBeanName;


}
