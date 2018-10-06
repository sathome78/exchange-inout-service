package com.exrates.inout.dao;

import com.exrates.inout.domain.StopOrder;
import com.exrates.inout.domain.dto.OrderBasicInfoDto;
import com.exrates.inout.domain.dto.OrderCreateDto;
import com.exrates.inout.domain.dto.OrderInfoDto;
import com.exrates.inout.domain.dto.OrderWideListDto;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.AdminStopOrderFilterData;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.main.CurrencyPair;
import com.exrates.inout.domain.main.PagingData;

import java.util.List;
import java.util.Locale;

/**
 * Created by maks on 20.04.2017.
 */
public interface StopOrderDao {
    boolean setStatus(int orderId, OrderStatus status);

    Integer create(StopOrder order);

    boolean setStatusAndChildOrderId(int orderId, Integer childOrderId, OrderStatus status);

    List<StopOrder> getOrdersBypairId(List<Integer> pairIds, OrderStatus opened);

    OrderCreateDto getOrderById(Integer orderId, boolean forUpdate);

    List<OrderWideListDto> getMyOrdersWithState(String email, CurrencyPair currencyPair, OrderStatus status, OperationType operationType, String scope, Integer offset, Integer limit, Locale locale);

    List<OrderWideListDto> getMyOrdersWithState(String email, CurrencyPair currencyPair, List<OrderStatus> statuses,
                                                OperationType operationType,
                                                String scope, Integer offset, Integer limit, Locale locale);

    PagingData<List<OrderBasicInfoDto>> searchOrders(AdminStopOrderFilterData adminOrderFilterData, DataTableParams dataTableParams, Locale locale);

    OrderInfoDto getStopOrderInfo(int orderId, Locale locale);
}
