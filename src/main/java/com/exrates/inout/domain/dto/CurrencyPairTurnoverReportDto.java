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
public class CurrencyPairTurnoverReportDto {
    private Integer orderNum;
    //wolper 19.04.2018
    //currency id added
    private int pairId;
    private String currencyPairName;
    private String currencyAccountingName;
    private OperationType operationType;
    private Integer quantity;
    private BigDecimal amountBase;
    private BigDecimal amountConvert;


    public static String getTitle() {
        return Stream.of("No.", "pair_id", "currency_pair", "currency_accounting", "operation_type", "quantity",
                "amount_base", "amount_convert")
                .collect(Collectors.joining(";", "", "\r\n"));
    }


    @Override
    public String toString() {
        return Stream.of(String.valueOf(orderNum), String.valueOf(pairId), currencyPairName, currencyAccountingName, operationType.name(), String.valueOf(quantity),
                BigDecimalProcessing.formatNoneComma(amountBase, false),
                BigDecimalProcessing.formatNoneComma(amountConvert, false))
                .collect(Collectors.joining(";", "", "\r\n"));
    }

}
