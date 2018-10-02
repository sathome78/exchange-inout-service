package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class WithdrawRequestInfoDto {

    private Integer requestId;

    private BigDecimal amount;

    private BigDecimal comissionAmount;

    private BigDecimal merchantComissionAmount;

    private String comissionAmountStr;

    private String amountStr;

    private String merchantComissionAmountStr;

    private String finalAmount;

    private String totalFee;

    private String userEmail;

    private String currencyName;

    private String merchantName;

    private String merchantDescription;

    private int delaySeconds;

    private String message;

    private WithdrawStatusEnum statusEnum;

    public void calculateFinalAmount() {
        finalAmount = amount.subtract(comissionAmount).subtract(merchantComissionAmount).setScale(8, RoundingMode.HALF_UP).toPlainString();
        totalFee = merchantComissionAmount.add(comissionAmount).setScale(8, RoundingMode.HALF_UP).toPlainString();
        amountStr = amount.setScale(8, RoundingMode.HALF_UP).toPlainString();
        comissionAmountStr = comissionAmount.setScale(8, RoundingMode.HALF_UP).toPlainString();
        merchantComissionAmountStr = merchantComissionAmount.setScale(8, RoundingMode.HALF_UP).toPlainString();
    }
}
