package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.Commission;
import com.exrates.inout.domain.main.Wallet;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Data
public class TransferDto {

    private Wallet walletUserFrom;
    private Wallet walletUserTo;
    private String userToNickName;
    private int currencyId;
    private int userFromId;
    private int userToId;
    private Commission commission;
    private String notyAmount;
    private BigDecimal initialAmount;
    private BigDecimal comissionAmount;
}
