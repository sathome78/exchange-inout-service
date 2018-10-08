package com.exrates.inout.service;

import com.exrates.inout.domain.StopOrder;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.enums.OrderActionEnum;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.main.ExOrder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.NavigableSet;

public interface StopOrderService {

    @Transactional
    String create(OrderCreateDto orderCreateDto, OrderActionEnum actionEnum, Locale locale);

    Integer createOrder(ExOrder exOrder);

    void proceedStopOrders(int pairId, NavigableSet<StopOrderSummaryDto> orders);

    @Transactional
    void proceedStopOrderAndRemove(int stopOrderId);

    List<StopOrder> getActiveStopOrdersByCurrencyPairsId(List<Integer> pairIds);

    @Transactional
    boolean cancelOrder(ExOrder exOrder, Locale locale);

    @Transactional
    boolean setStatus(int orderId, OrderStatus status);

    OrderCreateDto getOrderById(Integer orderId, boolean forUpdate);

    void onStopOrderCreate(ExOrder exOrder);

}
