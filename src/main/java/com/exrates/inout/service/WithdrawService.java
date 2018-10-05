package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.ClientBank;
import com.exrates.inout.domain.main.MerchantCurrency;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface WithdrawService {
  Map<String, String> createWithdrawalRequest(WithdrawRequestCreateDto requestCreateDto, Locale locale);

  void rejectError(int requestId, long timeoutInMinutes, String reasonCode);

  void rejectError(int requestId, String reasonCode);

  void rejectToReview(int requestId);

  void autoPostWithdrawalRequest(WithdrawRequestPostDto withdrawRequest);

  @Transactional
  void finalizePostWithdrawalRequest(Integer requestId);

  void postWithdrawalRequest(int requestId, Integer requesterAdminId, String txHash);

  List<ClientBank> findClientBanksForCurrency(Integer currencyId);

  List<WithdrawRequestFlatForReportDto> findAllByDateIntervalAndRoleAndCurrency(String startDate, String endDate, List<Integer> roleIdList, List<Integer> currencyList);

  void setAutoWithdrawParams(MerchantCurrencyOptionsDto merchantCurrencyOptionsDto);

  MerchantCurrencyAutoParamDto getAutoWithdrawParamsByMerchantAndCurrency(Integer merchantId, Integer currencyId);

  List<MerchantCurrency> retrieveAddressAndAdditionalParamsForWithdrawForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies);

  DataTable<List<WithdrawRequestsAdminTableDto>> getWithdrawRequestByStatusList(List<Integer> requestStatus, DataTableParams dataTableParams, WithdrawFilterData withdrawFilterData, String authorizedUserEmail, Locale locale);

  WithdrawRequestsAdminTableDto getWithdrawRequestById(Integer id, String authorizedUserEmail);

  void revokeWithdrawalRequest(int requestId);

  void takeInWorkWithdrawalRequest(int requestId, Integer requesterAdminId);

  void returnFromWorkWithdrawalRequest(int requestId, Integer requesterAdminId);

  void declineWithdrawalRequest(int requestId, Integer requesterAdminId, String comment);

  void confirmWithdrawalRequest(int requestId, Integer requesterAdminId);

  void setAllAvailableInPostingStatus();

  List<WithdrawRequestPostDto> dirtyReadForPostByStatusList(InvoiceStatus status);

  Map<String, String> correctAmountAndCalculateCommissionPreliminarily(Integer userId, BigDecimal amount, Integer currencyId, Integer merchantId, Locale locale, String destinationTag);

  boolean checkOutputRequestsLimit(int merchantId, String email);

  List<Integer> getWithdrawalStatistic(String startDate, String endDate);

  @Transactional(readOnly = true)
  List<WithdrawRequestFlatDto> getRequestsByMerchantIdAndStatus(int merchantId, List<Integer> statuses);

  @Transactional(readOnly = true)
  Optional<Integer> getRequestIdByHashAndMerchantId(String hash, int merchantId);

  @Transactional(readOnly = true)
  WithdrawRequestInfoDto getWithdrawalInfo(Integer id, Locale locale);
}
