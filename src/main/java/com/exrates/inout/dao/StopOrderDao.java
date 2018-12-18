package com.exrates.inout.dao;

import com.exrates.inout.domain.StopOrder;
import com.exrates.inout.domain.enums.OrderStatus;

/**
 * Created by maks on 20.04.2017.
 */
public interface StopOrderDao {

    boolean setStatus(int orderId, OrderStatus status);

    Integer create(StopOrder order);
}
