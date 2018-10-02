package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface RefillService {

    Map<String, Object> createRefillRequest(RefillRequestCreateDto requestCreateDto);

    Optional<String> getAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId);

    @Transactional(readOnly = true)
    Integer getMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId);

    List<MerchantCurrency> retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies, String userEmail);

    void confirmRefillRequest(InvoiceConfirmData invoiceConfirmData, Locale locale);

    void acceptRefillRequest(RefillRequestAcceptDto requestAcceptDto);

    RefillRequestFlatDto getFlatById(Integer id);

    void revokeRefillRequest(int requestId);

    List<InvoiceBank> findBanksForCurrency(Integer currencyId);

    Map<String, String> correctAmountAndCalculateCommission(Integer userId, BigDecimal amount, Integer currencyId, Integer merchantId, Locale locale);

    DataTable<List<RefillRequestsAdminTableDto>> getRefillRequestByStatusList(List<Integer> requestStatus, DataTableParams dataTableParams, RefillFilterData refillFilterData, String authorizedUserEmail, Locale locale);

    boolean checkInputRequestsLimit(int currencyId, String email);

    void takeInWorkRefillRequest(int requestId, Integer requesterAdminId);

    void returnFromWorkRefillRequest(int requestId, Integer requesterAdminId);

    void declineRefillRequest(int requestId, Integer requesterAdminId, String comment);

    RefillRequestsAdminTableDto getRefillRequestById(Integer id, String authorizedUserEmail);

    @Transactional
    Integer manualCreateRefillRequestCrypto(RefillRequestManualDto refillDto, Locale locale) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

    Optional<InvoiceBank> findInvoiceBankById(Integer id);

    String getPaymentMessageForTag(String serviceBeanName, String tag, Locale locale);
}
