package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.NotificatorTotalPriceDto;

import java.math.BigDecimal;

public interface NotificatorPriceDao {
    BigDecimal getFeeMessagePrice(int notificatorId, int roleId);
}
