package com.exrates.inout.dao;

import java.math.BigDecimal;

public interface NotificatorPriceDao {
    BigDecimal getFeeMessagePrice(int notificatorId, int roleId);
}
