package com.exrates.inout.service;

import com.exrates.inout.domain.StopOrder;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.AdminStopOrderFilterData;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.OrderActionEnum;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.main.CurrencyPair;
import com.exrates.inout.domain.main.ExOrder;
import com.exrates.inout.event.AcceptOrderEvent;
import com.exrates.inout.util.CacheData;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Locale;
import java.util.NavigableSet;

/**
 * Created by maks on 20.04.2017.
 */
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

    @TransactionalEventListener
    void onLimitOrderAccept(AcceptOrderEvent event);

    void onStopOrderCreate(ExOrder exOrder);

    @Transactional(readOnly = true)
    List<OrderWideListDto> getMyOrdersWithState(CacheData cacheData,
                                                String email, CurrencyPair currencyPair, OrderStatus status,
                                                OperationType operationType,
                                                String scope, Integer offset, Integer limit, Locale locale);

    @Transactional(readOnly = true)
    List<OrderWideListDto> getUsersStopOrdersWithStateForAdmin(String email, CurrencyPair currencyPair, OrderStatus status,
                                                               OperationType operationType,
                                                               Integer offset, Integer limit, Locale locale);

    @Transactional
    DataTable<List<OrderBasicInfoDto>> searchOrdersByAdmin(AdminStopOrderFilterData adminOrderFilterData, DataTableParams dataTableParams, Locale locale);

    /*@Transactional
    OrderInfoDto getStopOrderInfo(int orderId, Locale locale);*/

    @Transactional
    OrderInfoDto getStopOrderInfo(int orderId, Locale locale);

    Object deleteOrderByAdmin(int id, Locale locale);
}
