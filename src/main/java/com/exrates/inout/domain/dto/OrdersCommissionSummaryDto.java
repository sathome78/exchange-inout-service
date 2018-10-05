package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.util.BigDecimalProcessing;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class OrdersCommissionSummaryDto {
    private Integer orderNum;
    //wolper 19.04.18
    //currency id added
    private int pairId;
    private String currencyPairName;
    private String currencyAccountingName;
    private OperationType operationType;
    private BigDecimal amountBase;
    private BigDecimal amountConvert;
    private BigDecimal commissionAmount;


    public static String getTitle() {
        return Stream.of("No.", "pair_id", "currency_pair", "currency_accounting", "operation_type", "amount_base", "amount_convert", "commission_amount")
                .collect(Collectors.joining(";", "", "\r\n"));
    }


    @Override
    public String toString() {
        return Stream.of(String.valueOf(orderNum), String.valueOf(pairId), currencyPairName, currencyAccountingName, operationType.name(),
                BigDecimalProcessing.formatNoneComma(amountBase, false),
                BigDecimalProcessing.formatNoneComma(amountConvert, false),
                BigDecimalProcessing.formatNoneComma(commissionAmount, false))
                .collect(Collectors.joining(";", "", "\r\n"));
    }
}
