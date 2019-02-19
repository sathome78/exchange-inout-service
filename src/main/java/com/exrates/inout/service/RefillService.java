package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.domain.main.RefillRequestAddressShortDto;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface RefillService {

    Map<String, Object> createRefillRequest(RefillRequestCreateDto requestCreateDto);

    Optional<String> getAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId);

    List<String> getListOfValidAddressByMerchantIdAndCurrency(Integer merchantId, Integer currencyId);

    @Transactional(transactionManager = "slaveTxManager", readOnly = true)
    String getUsernameByRequestId(int requestId);

    @Transactional(readOnly = true)
    Integer getMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId);

    List<MerchantCurrency> retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies, String userEmail);

    Integer createRefillRequestByFact(RefillRequestAcceptDto request);

    @Transactional(transactionManager = "slaveTxManager", readOnly = true)
    Integer getRequestId(RefillRequestAcceptDto requestAcceptDto) throws RefillRequestAppropriateNotFoundException;

    void confirmRefillRequest(InvoiceConfirmData invoiceConfirmData, Locale locale);

    Optional<Integer> getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address,
            Integer merchantId,
            Integer currencyId,
            String hash);

    Optional<Integer> getRequestIdByMerchantIdAndCurrencyIdAndHash(
            Integer merchantId,
            Integer currencyId,
            String hash);

    Optional<RefillRequestFlatDto> findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address, Integer merchantId,
            Integer currencyId,
            String hash);

    Optional<Integer> getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId);

    Optional<Integer> getRequestIdInPendingByAddressAndMerchantIdAndCurrencyId(
            String address,
            Integer merchantId,
            Integer currencyId);

    List<RefillRequestFlatDto> getInExamineByMerchantIdAndCurrencyIdList(Integer merchantId, Integer currencyId);

    Optional<Integer> getUserIdByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId);

    void putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto onBchExamDto) throws RefillRequestAppropriateNotFoundException;

    void setConfirmationCollectedNumber(RefillRequestSetConfirmationsNumberDto confirmationsNumberDto) throws RefillRequestAppropriateNotFoundException;

    void autoAcceptRefillRequest(RefillRequestAcceptDto requestAcceptDto) throws RefillRequestAppropriateNotFoundException;

    void acceptRefillRequest(RefillRequestAcceptDto requestAcceptDto);

    RefillRequestFlatDto getFlatById(Integer id);

    void revokeRefillRequest(int requestId);

    List<InvoiceBank> findBanksForCurrency(Integer currencyId);

    Map<String, String> correctAmountAndCalculateCommission(Integer userId, BigDecimal amount, Integer currencyId, Integer merchantId, Locale locale);

    Integer clearExpiredInvoices() throws Exception;

    DataTable<List<RefillRequestsAdminTableDto>> getRefillRequestByStatusList(List<Integer> requestStatus, DataTableParams dataTableParams, RefillFilterData refillFilterData, String authorizedUserEmail, Locale locale);

    boolean checkInputRequestsLimit(int currencyId, String email);

    void takeInWorkRefillRequest(int requestId, Integer requesterAdminId);

    void returnFromWorkRefillRequest(int requestId, Integer requesterAdminId);

    void declineRefillRequest(int requestId, Integer requesterAdminId, String comment);

    Boolean existsClosedRefillRequestForAddress(String address, Integer merchantId, Integer currencyId);

    RefillRequestsAdminTableDto getRefillRequestById(Integer id, String authorizedUserEmail);

    @Transactional
    Integer manualCreateRefillRequestCrypto(RefillRequestManualDto refillDto, Locale locale) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

    Optional<RefillRequestBtcInfoDto> findRefillRequestByAddressAndMerchantTransactionId(String address,
                                                                                         String merchantTransactionId,
                                                                                         String merchantName,
                                                                                         String currencyName);

    Optional<String> getLastBlockHashForMerchantAndCurrency(Integer merchantId, Integer currencyId);

    Optional<InvoiceBank> findInvoiceBankById(Integer id);

    List<String> findAllAddresses(Integer merchantId, Integer currencyId);

    List<String> findAllAddresses(Integer merchantId, Integer currencyId, List<Boolean> isValidStatuses);

    String getPaymentMessageForTag(String serviceBeanName, String tag, Locale locale);

    int getTxOffsetForAddress(String address);

    void updateTxOffsetForAddress(String address, Integer offset);

    void updateAddressNeedTransfer(String address, Integer merchantId, Integer currencyId, boolean isNeeded);

    List<RefillRequestAddressDto> findAllAddressesNeededToTransfer(Integer merchantId, Integer currencyId);

    List<RefillRequestAddressDto> findByAddressMerchantAndCurrency(String address, Integer merchantId, Integer currencyId);

    List<Integer> getUnconfirmedTxsCurrencyIdsForTokens(int parentTokenId);

    List<RefillRequestFlatDto> getInExamineWithChildTokensByMerchantIdAndCurrencyIdList(int merchantId, int currencyId);

    List<RefillRequestAddressDto> findAddressDtos(Integer merchantId, Integer currencyId);

    void invalidateAddress(String address, Integer merchantId, Integer currencyId);
}
