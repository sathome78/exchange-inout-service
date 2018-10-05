package com.exrates.inout.exceptions;

import com.exrates.inout.domain.main.CurrencyPair;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExOrderStatisticsDto {
    private CurrencyPair currencyPair;
    private String firstOrderAmountBase;
    private String firstOrderRate;
    private String lastOrderAmountBase;
    private String lastOrderRate;
    private String percentChange;
    private String minRate;
    private String maxRate;
    private String sumBase;
    private String sumConvert;

    public ExOrderStatisticsDto() {
    }

    public ExOrderStatisticsDto(CurrencyPair currencyPair) {
        this.currencyPair = currencyPair;
    }

    public ExOrderStatisticsDto(ExOrderStatisticsDto exOrderStatisticsDto) {
        this.currencyPair = exOrderStatisticsDto.currencyPair;
        this.firstOrderAmountBase = exOrderStatisticsDto.firstOrderAmountBase;
        this.firstOrderRate = exOrderStatisticsDto.firstOrderRate;
        this.lastOrderAmountBase = exOrderStatisticsDto.lastOrderAmountBase;
        this.lastOrderRate = exOrderStatisticsDto.lastOrderRate;
        this.percentChange = exOrderStatisticsDto.percentChange;
        this.minRate = exOrderStatisticsDto.minRate;
        this.maxRate = exOrderStatisticsDto.maxRate;
        this.sumBase = exOrderStatisticsDto.sumBase;
        this.sumConvert = exOrderStatisticsDto.sumConvert;
    }
}
