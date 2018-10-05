package com.exrates.inout.domain;

import com.exrates.inout.domain.dto.CandleChartItemDto;

import java.util.List;

public interface ChartsCacheInterface {

    List<CandleChartItemDto> getData();

    List<CandleChartItemDto> getLastData();

    void setNeedToUpdate();
}
