package com.exrates.inout.domain.dto;

import lombok.Data;

@Data
public class BtcWalletInfoDto {
    private String balance;
    private String confirmedNonSpendableBalance;
    private String unconfirmedBalance;
    private Integer transactionCount;
}
