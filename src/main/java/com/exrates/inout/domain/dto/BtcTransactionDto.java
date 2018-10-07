package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BtcTransactionDto {

    private BigDecimal amount;
    private BigDecimal fee;
    private Integer confirmations;
    private String txId;
    private String blockhash;
    private List<String> walletConflicts;
    private Long time;
    private Long timeReceived;
    private String comment;
    private String to;
    private List<BtcTxPaymentDto> details;

}
