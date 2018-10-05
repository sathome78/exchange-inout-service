package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.ChartResolutionTimeUnit;
import com.exrates.inout.util.ChartResolutionSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@EqualsAndHashCode
@JsonSerialize(using = ChartResolutionSerializer.class)
public class ChartResolution {

    private final int timeValue;
    private final ChartResolutionTimeUnit timeUnit;
    private final int seconds;

    public ChartResolution(int timeValue, ChartResolutionTimeUnit timeUnit) {
        this.timeValue = timeValue;
        this.timeUnit = timeUnit;
        this.seconds = timeValue * timeUnit.getSecondsValue();
    }

    @Override
    public String toString() {
        return String.join("", String.valueOf(timeValue), timeUnit.getShortName());
    }

    public static ChartResolution ofString(String resolutionString) {
        if (StringUtils.isEmpty(resolutionString)) {
            throw new IllegalArgumentException("Empty resolution string");
        }
        char resolutionTypeChar = resolutionString.charAt(resolutionString.length() - 1);
        if (Character.isDigit(resolutionTypeChar)) {
            return new ChartResolution(Integer.parseInt(resolutionString), ChartResolutionTimeUnit.MINUTE);
        }

        String resolutionValueString = resolutionString.substring(0, resolutionString.length() - 1);
        int resolutionValue;
        if (resolutionValueString.isEmpty()) {
            resolutionValue = 1;
        } else {
            resolutionValue = Integer.parseInt(resolutionValueString);
        }
        return new ChartResolution(resolutionValue, ChartResolutionTimeUnit.fromShortName(String.valueOf(resolutionTypeChar)));
    }

}
