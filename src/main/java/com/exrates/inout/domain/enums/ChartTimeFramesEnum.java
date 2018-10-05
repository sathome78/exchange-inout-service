package com.exrates.inout.domain.enums;


import com.exrates.inout.domain.dto.ChartResolution;
import com.exrates.inout.domain.dto.ChartTimeFrame;

import java.util.Arrays;

import static com.exrates.inout.domain.enums.IntervalType2.DAY;
import static com.exrates.inout.domain.enums.IntervalType2.MONTH;

public enum ChartTimeFramesEnum {
//    HOUR_6(new ChartTimeFrame(new ChartResolution(1, ChartResolutionTimeUnit.MINUTE), 6, HOUR)),
//    HOUR_12(new ChartTimeFrame(new ChartResolution(10, ChartResolutionTimeUnit.MINUTE), 12, HOUR)),
    DAY_5(new ChartTimeFrame(new com.exrates.inout.domain.dto.ChartResolution(30, ChartResolutionTimeUnit.MINUTE), 5, DAY)),
    DAY_7(new ChartTimeFrame(new com.exrates.inout.domain.dto.ChartResolution(60, ChartResolutionTimeUnit.MINUTE), 7, DAY)),
    DAY_10(new ChartTimeFrame(new com.exrates.inout.domain.dto.ChartResolution(240, ChartResolutionTimeUnit.MINUTE), 10, DAY)),
    DAY_15(new ChartTimeFrame(new com.exrates.inout.domain.dto.ChartResolution(720, ChartResolutionTimeUnit.MINUTE), 15, DAY)),
    MONTH_7(new ChartTimeFrame(new com.exrates.inout.domain.dto.ChartResolution(1, ChartResolutionTimeUnit.DAY), 7, MONTH));



    private ChartTimeFrame timeFrame;

    ChartTimeFramesEnum(ChartTimeFrame timeFrame) {
        this.timeFrame = timeFrame;
    }

    public ChartTimeFrame getTimeFrame() {
        return timeFrame;
    }

    public com.exrates.inout.domain.dto.ChartResolution getResolution() {
        return timeFrame.getResolution();
    }

    public static ChartTimeFramesEnum ofResolution(String resolutionString) {
        ChartResolution chartResolution = ChartResolution.ofString(resolutionString);
        return Arrays.stream(ChartTimeFramesEnum.values()).filter(item -> item.getResolution().equals(chartResolution))
                .findFirst().orElseThrow(() -> new IllegalArgumentException(resolutionString));
    }
}
