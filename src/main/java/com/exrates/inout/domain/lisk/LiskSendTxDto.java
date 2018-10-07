package com.exrates.inout.domain.lisk;

import lombok.Data;

@Data
public class LiskSendTxDto {
    private String secret;
    private Long amount;
    private String recipientId;
}
