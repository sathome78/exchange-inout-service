package com.exrates.inout.dao;

import com.exrates.inout.domain.AdminOrderFilterData;
import com.exrates.inout.domain.BackDealInterval;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.enums.*;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.exceptions.ExOrderStatisticsDto;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface OrderDao {

    int createOrder(ExOrder order);

    Optional<BigDecimal> getLastOrderPriceByCurrencyPairAndOperationType(int currencyPairId, int operationTypeId);

    Optional<BigDecimal> getLowestOpenOrderPriceByCurrencyPairAndOperationType(int currencyPairId, int operationTypeId);

    ExOrder getOrderById(int orderid);

    boolean setStatus(int orderId, OrderStatus status);

    boolean updateOrder(ExOrder exOrder);

    List<OrderListDto> getOrdersBuyForCurrencyPair(CurrencyPair currencyPair, UserRole filterRole);

    void postAcceptedOrderToDB(ExOrder exOrder);

    List<OrderListDto> getOrdersSellForCurrencyPair(CurrencyPair currencyPair, UserRole filterRole);

    List<Map<String, Object>> getDataForAreaChart(CurrencyPair currencyPair, BackDealInterval backDealInterval);

    List<CandleChartItemDto> getDataForCandleChart(CurrencyPair currencyPair, BackDealInterval backDealInterval);
  
  List<CandleChartItemDto> getDataForCandleChart(CurrencyPair currencyPair, BackDealInterval backDealInterval, LocalDateTime endTime);

    List<CandleChartItemDto> getDataForCandleChart(CurrencyPair currencyPair, LocalDateTime startTime, LocalDateTime endTime, int resolutionValue, String resolutionType);

    ExOrderStatisticsDto getOrderStatistic(CurrencyPair currencyPair, BackDealInterval backDealInterval);

    List<ExOrderStatisticsShortByPairsDto> getOrderStatisticByPairs();

    List<ExOrderStatisticsShortByPairsDto> getOrderStatisticForSomePairs(List<Integer> pairsIds);

    List<CoinmarketApiDto> getCoinmarketData(String currencyPairName);

    OrderInfoDto getOrderInfo(int orderId, Locale locale);

    int searchOrderByAdmin(Integer currencyPair, Integer orderType, String orderDate, BigDecimal orderRate, BigDecimal orderVolume);

    List<OrderAcceptedHistoryDto> getOrderAcceptedForPeriod(String email, BackDealInterval backDealInterval, Integer limit, CurrencyPair currencyPair);

    OrderCommissionsDto getCommissionForOrder(UserRole userRole);

    CommissionsDto getAllCommissions(UserRole userRole);

    List<OrderWideListDto> getMyOrdersWithState(Integer userId, CurrencyPair currencyPair, OrderStatus status,
                                                OperationType operationType,
                                                String scope, Integer offset, Integer limit, Locale locale);

    List<OrderWideListDto> getMyOrdersWithState(Integer userId, CurrencyPair currencyPair, List<OrderStatus> statuses,
                                                OperationType operationType,
                                                String scope, Integer offset, Integer limit, Locale locale);

    OrderCreateDto getMyOrderById(int orderId);

    WalletsAndCommissionsForOrderCreationDto getWalletAndCommission(String email, Currency currency,
                                                                    OperationType operationType, UserRole userRole);

    boolean lockOrdersListForAcception(List<Integer> ordersList);

    PagingData<List<OrderBasicInfoDto>> searchOrders(AdminOrderFilterData adminOrderFilterData, DataTableParams dataTableParams, Locale locale);

    List<ExOrder> selectTopOrders(Integer currencyPairId, BigDecimal exrate, OperationType orderType, boolean sameRoleOnly, Integer userAcceptorRoleId, OrderBaseType orderBaseType);

    List<UserSummaryOrdersByCurrencyPairsDto> getUserSummaryOrdersByCurrencyPairList(Integer requesterUserId, String startDate, String endDate, List<Integer> roles);

    List<CurrencyPairTurnoverReportDto> getCurrencyPairTurnoverForPeriod(LocalDateTime startTime, LocalDateTime endTime, List<Integer> userRoleIdList);

    List<OrdersCommissionSummaryDto> getOrderCommissionsByPairsForPeriod(LocalDateTime startTime, LocalDateTime endTime, List<Integer> userRoleIdList);

    OrderRoleInfoForDelete getOrderRoleInfo(int orderId);

    List<RatesUSDForReportDto> getRatesToUSDForReport();

    List<OrderBookItem> getOrderBookItemsForType(Integer currencyPairId, OrderType orderType);

    List<OrderBookItem> getOrderBookItems(Integer currencyPairId);

    List<OpenOrderDto> getOpenOrders(Integer currencyPairId, OrderType orderType);

    List<OrderHistoryItem> getRecentOrderHistory(Integer currencyPairId, BackDealInterval interval);

    List<UserOrdersDto> getUserOpenOrders(Integer userId, Integer currencyPairId);

    List<UserOrdersDto> getUserOrdersHistory(Integer userId, @Nullable Integer currencyPairId, int limit, int offset);
}
