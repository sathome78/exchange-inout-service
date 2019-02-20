package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.TronTransactionTypeEnum;
import lombok.Data;

@Data
public class TronReceivedTransactionDto {

    private Integer id;
    private String amount;
    private String hash;
    /*HEX address*/
    private String address;
    /*Base58 address*/
    private String addressBase58;
    private boolean isConfirmed;
    private long rawAmount;
    private TronTransactionTypeEnum txType;
    private String assetName;
    private int merchantId;
    private int currencyId;


    public TronReceivedTransactionDto(long rawAmount, String hash, String address) {
        this.rawAmount = rawAmount;
        this.hash = hash;
        this.address = address;
    }


}
