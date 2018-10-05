package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.IntervalType2;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ChartTimeFrame {
    private final ChartResolution resolution;
    private final int timeValue;
    private final IntervalType2 timeUnit;

    public String getShortName() {
        return String.join("", String.valueOf(timeValue), timeUnit.getShortName().toLowerCase());
    }
}
