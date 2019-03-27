package com.exrates.inout.domain.dto;

import com.exrates.inout.util.BigDecimalProcessing;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class CurrencyInputOutputSummaryDto {
    private Integer orderNum;
    //wolper 19.04.18
    //currency id added
    private int curId;
    //wolper 24.04.18
    private BigDecimal rateToUSD;
    private String currencyName;
    private BigDecimal input;
    private BigDecimal output;
    private BigDecimal diff;

    public void calculateAndSetDiff() {
        diff = input.subtract(output);
    }

    public static String getTitle() {
        return Stream.of("No.", "cur_id", "currency", "rateToUSD", "input", "output", "diff")
                .collect(Collectors.joining(";", "", "\r\n"));
    }


    @Override
    public String toString() {
        return Stream.of(String.valueOf(orderNum), String.valueOf(curId),  currencyName, BigDecimalProcessing.formatNoneComma(rateToUSD, false),
                BigDecimalProcessing.formatNoneComma(input, false),
                BigDecimalProcessing.formatNoneComma(output, false),
                BigDecimalProcessing.formatNoneComma(diff, false))
                .collect(Collectors.joining(";", "", "\r\n"));
    }
}
