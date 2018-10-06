package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RefillRequestDao {

    Optional<Integer> create(RefillRequestCreateDto request);

    Optional<Integer> findUserIdByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId);

    Optional<String> findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId);

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    void setStatusById(Integer id, InvoiceStatus newStatus);

    void setStatusAndConfirmationDataById(Integer id, InvoiceStatus newStatus, InvoiceConfirmData invoiceConfirmData);

    void setMerchantRequestSignById(Integer id, String sign);

    List<InvoiceBank> findInvoiceBankListByCurrency(Integer currencyId);

    Optional<InvoiceBank> findInvoiceBankById(Integer id);

    Optional<RefillRequestFlatDto> getFlatByIdAndBlock(Integer id);

    Optional<RefillRequestFlatDto> getFlatById(Integer id);

    PagingData<List<RefillRequestFlatDto>> getPermittedFlatByStatus(List<Integer> statusIdList, Integer requesterUserId, DataTableParams dataTableParams, RefillFilterData refillFilterData);

    RefillRequestFlatDto getPermittedFlatById(Integer id, Integer requesterUserId);

    RefillRequestFlatAdditionalDataDto getAdditionalDataForId(int id);

    void setHolderById(Integer id, Integer holderId);

    void setRemarkById(Integer id, String remark);

    void setMerchantTransactionIdById(Integer id, String merchantTransactionId) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

    boolean checkInputRequests(int currencyId, String email);

    Optional<Integer> findUserIdById(Integer requestId);

    boolean isToken(Integer merchantId);

    List<Map<String, Integer>> getTokenMerchants(Integer merchantId);

    Integer findMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId);

    List<String> findAllAddresses(Integer merchantId, Integer currencyId);

    Optional<Integer> findIdWithoutConfirmationsByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    void setConfirmationsNumberByRequestId(Integer requestId, BigDecimal amount, Integer confirmations, String blockhash);

    Optional<RefillRequestFlatDto> findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address, Integer merchantId,
            Integer currencyId,
            String hash);

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndHash(String address, Integer merchantId, Integer currencyId, String hash);

    Optional<Integer> findIdByMerchantIdAndCurrencyIdAndHash(Integer merchantId, Integer currencyId, String hash);

    List<RefillRequestAddressDto> findAddressDtosByMerchantAndCurrency(Integer merchantId, Integer currencyId);

    void updateAddressNeedTransfer(String address, int merchantId, int currencyId, boolean isNeeded);

    List<RefillRequestFlatDto> findAllWithChildTokensWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(int merchantId, int currencyId, List<Integer> collect);

    List<RefillRequestAddressDto> findAllAddressesNeededToTransfer(Integer merchantId, Integer currencyId);

    List<RefillRequestFlatDto> findAllWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(Integer merchantId, Integer currencyId, List<Integer> collect);

    int getTxOffsetForAddress(String address);

    void updateTxOffsetForAddress(String address, Integer offset);

    List<RefillRequestAddressDto> findByAddressMerchantAndCurrency(String address, int merchantId, int currencyId);

    Optional<String> getLastBlockHashForMerchantAndCurrency(Integer merchantId, Integer currencyId);

    Optional<RefillRequestBtcInfoDto> findRefillRequestByAddressAndMerchantTransactionId(String address, String merchantTransactionId, Integer merchantId, Integer currencyId);

    List<Integer> getUnconfirmedTxsCurrencyIdsForTokens(int parentTokenId);

}
