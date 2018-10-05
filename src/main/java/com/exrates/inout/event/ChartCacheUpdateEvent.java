package com.exrates.inout.event;

import com.exrates.inout.domain.dto.ChartTimeFrame;
import org.springframework.context.ApplicationEvent;

public class ChartCacheUpdateEvent extends ApplicationEvent {

    private ChartTimeFrame timeFrame;

    private int pairId;

    public ChartCacheUpdateEvent(Object source, ChartTimeFrame timeFrame, int pairId) {
        super(source);
        this.timeFrame = timeFrame;
        this.pairId = pairId;
    }

    public ChartTimeFrame getTimeFrame() {
        return timeFrame;
    }

    public int getPairId() {
        return pairId;
    }
}
