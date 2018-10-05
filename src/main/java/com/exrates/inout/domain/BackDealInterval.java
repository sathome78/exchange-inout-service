package com.exrates.inout.domain;

import com.exrates.inout.domain.enums.IntervalType;
import com.exrates.inout.exceptions.UnsupportedIntervalFormatException;
import com.exrates.inout.exceptions.UnsupportedIntervalTypeException;

public class BackDealInterval {
    public Integer intervalValue;
    public IntervalType intervalType;

    public BackDealInterval(Integer intervalValue, IntervalType intervalType) {
        this.intervalValue = intervalValue;
        this.intervalType = intervalType;
    }

    /*constructor*/
    public BackDealInterval(String intervalString) {
        try {
            this.intervalValue = Integer.valueOf(intervalString.split(" ")[0]);
            this.intervalType = IntervalType.convert(intervalString.split(" ")[1]);
        } catch (UnsupportedIntervalTypeException e) {
            throw e;
        } catch (Exception e) {
            throw new UnsupportedIntervalFormatException(intervalString);
        }
    }
    /**/
    public String getInterval(){
        return intervalValue+" "+intervalType;
    }
    /*getters setters*/

    @Override
    public String toString() {
        return "BackDealInterval{" +
                "intervalValue=" + intervalValue +
                ", intervalType=" + intervalType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BackDealInterval that = (BackDealInterval) o;

        if (intervalValue != null ? !intervalValue.equals(that.intervalValue) : that.intervalValue != null)
            return false;
        return intervalType == that.intervalType;

    }

    @Override
    public int hashCode() {
        int result = intervalValue != null ? intervalValue.hashCode() : 0;
        result = 31 * result + (intervalType != null ? intervalType.hashCode() : 0);
        return result;
    }
}
