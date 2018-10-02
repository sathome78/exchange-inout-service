package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RefillRequestDao {

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    Optional<Integer> findIdWithoutConfirmationsByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndHash(String address, Integer merchantId, Integer currencyId, String hash);

    Optional<Integer> findUserIdByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId);

    Optional<Integer> create(RefillRequestCreateDto request);

    Optional<String> findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId);

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

}
