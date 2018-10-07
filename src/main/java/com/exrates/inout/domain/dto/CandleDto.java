package com.exrates.inout.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CandleDto {
    private long time;
    private BigDecimal close;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal volume;

    public CandleDto(CandleChartItemDto candleChartItemDto) {
        this.time = candleChartItemDto.getBeginDate().getTime();
        this.close = candleChartItemDto.getCloseRate();
        this.open = candleChartItemDto.getOpenRate();
        this.high = candleChartItemDto.getHighRate();
        this.low = candleChartItemDto.getLowRate();
        this.volume = candleChartItemDto.getBaseVolume();
    }
}
