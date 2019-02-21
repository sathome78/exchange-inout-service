package com.exrates.inout.service.monero;

import com.exrates.inout.service.monero.utils.MoneroUtils;
import lombok.EqualsAndHashCode;
//exrates.service.monero.utils.MoneroUtils;

@EqualsAndHashCode
public class MoneroAddress {
    private String standardAddress;

    public MoneroAddress(String standardAddress) {
        MoneroUtils.validateStandardAddress(standardAddress);
        this.standardAddress = standardAddress;
    }

    public String getStandardAddress() {
        return this.standardAddress;
    }

    public String toString() {
        return this.standardAddress;
    }

}
