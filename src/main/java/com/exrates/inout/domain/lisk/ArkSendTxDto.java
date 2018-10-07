package com.exrates.inout.domain.lisk;

import lombok.Data;

@Data
public class    ArkSendTxDto {
    private String passphrase;
    private Long amount;
    private String recipientId;
}
