package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.other.WalletOperationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class WalletOperationMsDto {

    private WalletOperationData walletOperationData;
    private int currencyId;
}

