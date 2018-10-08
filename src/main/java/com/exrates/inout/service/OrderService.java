package com.exrates.inout.service;

import com.exrates.inout.domain.AdminOrderFilterData;
import com.exrates.inout.domain.BackDealInterval;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.ChartResolution;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.enums.*;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.exceptions.ExOrderStatisticsDto;
import com.exrates.inout.util.CacheData;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface OrderService {


    List<ExOrderStatisticsShortByPairsDto> getOrdersStatisticByPairsEx(RefreshObjectsEnum refreshObjectsEnum);

    List<ExOrderStatisticsShortByPairsDto> getStatForSomeCurrencies(List<Integer> pairsIds);

    OrderCreateDto prepareNewOrder(CurrencyPair activeCurrencyPair, OperationType orderType, String userEmail, BigDecimal amount, BigDecimal rate, OrderBaseType baseType);

    OrderCreateDto prepareNewOrder(CurrencyPair activeCurrencyPair, OperationType orderType, String userEmail, BigDecimal amount, BigDecimal rate, Integer sourceId, OrderBaseType baseType);

    OrderValidationDto validateOrder(OrderCreateDto orderCreateDto);

    @Transactional
    Integer createOrderByStopOrder(OrderCreateDto orderCreateDto, OrderActionEnum action, Locale locale);

    int createOrder(OrderCreateDto order, OrderActionEnum action);

    @Transactional
    OrderCreateDto prepareOrderRest(OrderCreationParamsDto orderCreationParamsDto, String userEmail, Locale locale, OrderBaseType orderBaseType);

    @Transactional
    OrderCreationResultDto createPreparedOrderRest(OrderCreateDto orderCreateDto, Locale locale);

    Optional<String> autoAccept(OrderCreateDto orderCreateDto, Locale locale);

    Optional<OrderCreationResultDto> autoAcceptOrders(OrderCreateDto orderCreateDto, Locale locale);

    ExOrder getOrderById(int orderId);

    boolean setStatus(int orderId, OrderStatus status);

    void acceptOrdersList(int userAcceptorId, List<Integer> ordersList, Locale locale);

    boolean cancellOrder(ExOrder exOrder, Locale locale);

    boolean updateOrder(ExOrder exOrder);

    List<CoinmarketApiDto> getCoinmarketDataForActivePairs(String currencyPairName, BackDealInterval backDealInterval);

    OrderInfoDto getOrderInfo(int orderId, Locale locale);

    Object deleteOrderByAdmin(int orderId);

    Object deleteOrderForPartialAccept(int orderId);

    List<ChartTimeFrame> getChartTimeFrames();

    @Transactional
    List<CandleChartItemDto> getLastDataForCandleChart(Integer currencyPairId,
                                                       LocalDateTime startTime, ChartResolution resolution);

    List<CandleChartItemDto> getDataForCandleChart(int pairId, ChartTimeFrame timeFrame);

    List<CandleChartItemDto> getDataForCandleChart(CurrencyPair currencyPair, BackDealInterval interval);

    @Transactional
    List<OrderAcceptedHistoryDto> getOrderAcceptedForPeriodEx(String email,
                                                              BackDealInterval backDealInterval,
                                                              Integer limit, CurrencyPair currencyPair, Locale locale);

    @Transactional(readOnly = true)
    List<OrderListDto> getAllBuyOrdersEx(CurrencyPair currencyPair, Locale locale, UserRole userRole);

    @Transactional(readOnly = true)
    List<OrderListDto> getAllSellOrdersEx(CurrencyPair currencyPair, Locale locale, UserRole userRole);

    WalletsAndCommissionsForOrderCreationDto getWalletAndCommission(String email, Currency currency,
                                                                    OperationType operationType);

}
