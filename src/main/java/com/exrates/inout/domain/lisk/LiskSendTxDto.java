package com.exrates.inout.domain.lisk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LiskSendTxDto {
    private Long amount;
    private String recipientId;
    private String passphrase;
}
