package com.exrates.inout.domain.dto;

import java.math.BigDecimal;

public class OrderCommissionsDto {
    private BigDecimal sellCommission;
    private BigDecimal buyCommission;
    /*getters setters*/

    public BigDecimal getSellCommission() {
        return sellCommission;
    }

    public void setSellCommission(BigDecimal sellCommission) {
        this.sellCommission = sellCommission;
    }

    public BigDecimal getBuyCommission() {
        return buyCommission;
    }

    public void setBuyCommission(BigDecimal buyCommission) {
        this.buyCommission = buyCommission;
    }
}
