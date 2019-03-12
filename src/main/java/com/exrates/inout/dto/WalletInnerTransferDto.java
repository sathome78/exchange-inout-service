package com.exrates.inout.dto;

import com.exrates.inout.domain.enums.TransactionSourceType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class WalletInnerTransferDto {
    private int walletId;
    private BigDecimal amount;
    private TransactionSourceType sourceType;
    private int sourceId;
    private String description;
}
