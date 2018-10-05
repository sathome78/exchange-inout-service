package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.main.AdminTransactionsFilterData;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.util.CacheData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface TransactionService {

  Transaction createTransactionRequest(CreditsOperation creditsOperation);

  Transaction findById(int id);

  void updateTransactionAmount(Transaction transaction, BigDecimal amount);

  void updateTransactionAmount(Transaction transaction);

  BigDecimal calculateNewCommission(Transaction transaction, BigDecimal amount);

  void nullifyTransactionAmountForWithdraw(Transaction transaction);

  void updateTransactionConfirmation(int transactionId, int confirmations);

  void provideTransaction(Transaction transaction);

  void invalidateTransaction(Transaction transaction);

  DataTable<List<OperationViewDto>> showUserOperationHistory(Integer requesterUserId, Integer userId, AdminTransactionsFilterData filterData, DataTableParams dataTableParams, Locale locale);

  List<AccountStatementDto> getAccountStatement(CacheData cacheData, Integer walletId, Integer offset, Integer limit, Locale locale);

  DataTable<List<AccountStatementDto>> getAccountStatementForAdmin(Integer walletId, Integer offset, Integer limit, Locale locale);

  BigDecimal maxAmount();

  BigDecimal maxCommissionAmount();

  List<AccountStatementDto> getAccountStatement(Integer walletId, Integer offset, Integer limit, Locale locale);

  void setSourceId(Integer trasactionId, Integer sourceId);

  List<TransactionFlatForReportDto> getAllByDateIntervalAndRoleAndOperationTypeAndCurrencyAndSourceType(String startDate, String endDate, Integer operationType, List<Integer> roleIdList, List<Integer> currencyList, List<String> sourceTypeList);

  boolean setStatusById(Integer trasactionId, Integer statusId);

  List<UserSummaryDto> getTurnoverInfoByUserAndCurrencyForPeriodAndRoleList(Integer requesterUserId, String startDate, String endDate, List<Integer> roleIdList);

  List<UserSummaryOrdersDto> getUserSummaryOrdersList(Integer requesterUserId, String startDate, String endDate, List<Integer> roles);

    List<Transaction> getPayedRefTransactionsByOrderId(int orderId);
}
