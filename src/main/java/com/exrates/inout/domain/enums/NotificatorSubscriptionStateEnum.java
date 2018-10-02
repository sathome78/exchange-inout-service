package com.exrates.inout.domain.enums;

public enum NotificatorSubscriptionStateEnum {

    SUBSCRIBED(false, true, null),
    WAIT_FOR_SUBSCRIBE(true, false, SUBSCRIBED);

    public static NotificatorSubscriptionStateEnum getFinalState() {
        return SUBSCRIBED;
    }

    NotificatorSubscriptionStateEnum(boolean beginState, boolean finalState, NotificatorSubscriptionStateEnum nextState) {
        this.beginState = beginState;
        this.nextState = nextState;
        this.finalState = finalState;
    }

    boolean beginState;

    boolean finalState;

    NotificatorSubscriptionStateEnum nextState;


    public NotificatorSubscriptionStateEnum getNextState() {
        return SUBSCRIBED;
    }

    public boolean isBeginState() {
        return beginState;
    }

    public boolean isFinalState() {
        return finalState;
    }
}
