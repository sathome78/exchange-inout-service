package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.AccountStatementDto;
import com.exrates.inout.domain.dto.TransactionFlatForReportDto;
import com.exrates.inout.domain.dto.UserSummaryDto;
import com.exrates.inout.domain.dto.UserSummaryOrdersDto;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.main.AdminTransactionsFilterData;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.domain.main.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface TransactionDao {


    Transaction create(Transaction transaction);

    boolean updateForProvided(Transaction transaction);

    Transaction findById(int id);

    PagingData<List<Transaction>> findAllByUserWallets(
            Integer requesterUserId, List<Integer> userWalletIds, AdminTransactionsFilterData filterData, DataTableParams dataTableParams, Locale locale);

    boolean provide(int id);

    boolean delete(int id);

    void updateTransactionAmount(int transactionId, BigDecimal amount, BigDecimal commission);

    void updateTransactionConfirmations(int transactionId, int confirmations);

    List<AccountStatementDto> getAccountStatement(Integer walletId, Integer offset, Integer limit, Locale locale);

    Integer getStatementSize(Integer walletId);

    BigDecimal maxAmount();

    BigDecimal maxCommissionAmount();

    void setSourceId(Integer trasactionId, Integer sourceId);

    List<TransactionFlatForReportDto> findAllByDateIntervalAndRoleAndOperationTypeAndCurrencyAndSourceType(String startDate, String endDate, Integer operationType, List<Integer> roleIdList, List<Integer> currencyList, List<String> sourceTypeList);

    boolean setStatusById(Integer trasactionId, Integer statusId);

    List<UserSummaryDto> getTurnoverInfoByUserAndCurrencyForPeriodAndRoleList(Integer requesterUserId, String startDate, String endDate, List<Integer> roleIdList);

    List<UserSummaryOrdersDto> getUserSummaryOrdersList(Integer requesterUserId, String startDate, String endDate, List<Integer> roles);

    List<Transaction> getPayedRefTransactionsByOrderId(int orderId);
}
