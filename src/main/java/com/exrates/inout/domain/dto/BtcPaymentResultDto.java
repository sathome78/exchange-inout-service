package com.exrates.inout.domain.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BtcPaymentResultDto {
    private String txId;
    private String error;

    public BtcPaymentResultDto(String txId) {
        this.txId = txId;
    }

    public BtcPaymentResultDto(Exception e) {
        this.error = e.getMessage();
    }
}
