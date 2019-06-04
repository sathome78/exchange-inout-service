package com.exrates.inout.service.usdx.model;

import com.exrates.inout.domain.serializer.LocalDateTimeDeserializer;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.exrates.inout.service.usdx.model.enums.UsdxTransactionStatus;
import com.exrates.inout.service.usdx.model.enums.UsdxTransactionType;
import com.exrates.inout.service.usdx.model.enums.UsdxWalletAsset;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UsdxTransaction {
    private String transferId;
    private String accountName;
    private BigDecimal amount;
    private UsdxWalletAsset currency;
    private UsdxTransactionType type;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;
    private UsdxTransactionStatus status;
    private String memo;
    private String customData;

    private String errorCode;
    private String failReason;
}
