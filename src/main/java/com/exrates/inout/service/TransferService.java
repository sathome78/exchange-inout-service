package com.exrates.inout.service;

import com.exrates.inout.domain.dto.TransferDto;
import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.domain.dto.TransferRequestFlatDto;
import com.exrates.inout.domain.dto.VoucherAdminTableDto;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.VoucherFilterData;
import com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum;
import com.exrates.inout.domain.main.MerchantCurrency;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface TransferService {
    Map<String, Object> createTransferRequest(TransferRequestCreateDto request);

    List<MerchantCurrency> retrieveAdditionalParamsForWithdrawForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies);

    void revokeByUser(int requestId, Principal principal);

    void revokeByAdmin(int requestId, Principal principal);

    @Transactional(readOnly = true)
    List<TransferRequestFlatDto> getRequestsByMerchantIdAndStatus(int merchantId, List<Integer> statuses);

    TransferRequestFlatDto getFlatById(Integer id);

    Map<String, String> correctAmountAndCalculateCommissionPreliminarily(Integer userId, BigDecimal amount, Integer currencyId, Integer merchantId, Locale locale);

    Optional<TransferRequestFlatDto> getByHashAndStatus(String code, Integer requiredStatus, boolean block);

    boolean checkRequest(TransferRequestFlatDto transferRequestFlatDto, String userEmail);

    TransferDto performTransfer(TransferRequestFlatDto transferRequestFlatDto, Locale locale, InvoiceActionTypeEnum action);

    String getUserEmailByTrnasferId(int id);

    @Transactional
    DataTable<List<VoucherAdminTableDto>> getAdminVouchersList(
            DataTableParams dataTableParams,
            VoucherFilterData withdrawFilterData,
            String authorizedUserEmail,
            Locale locale);

    String getHash(Integer id, Principal principal);
}
