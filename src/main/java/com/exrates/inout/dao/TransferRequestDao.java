package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.domain.dto.TransferRequestFlatDto;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.VoucherFilterData;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.PagingData;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TransferRequestDao {
    int create(TransferRequestCreateDto transferRequest);

    Optional<TransferRequestFlatDto> getFlatByIdAndBlock(int id);

    Optional<TransferRequestFlatDto> getFlatById(int id);

    Optional<TransferRequestFlatDto> getFlatByHashAndStatus(String hash, Integer requiredStatus, boolean block);

    void setStatusById(Integer id, InvoiceStatus newStatus);

    void setRecipientById(Integer id, Integer recipientId);

    List<TransferRequestFlatDto> findRequestsByStatusAndMerchant(Integer merchantId, List<Integer> statusId);

    void setHashById(Integer id, Map<String, String> params);

    String getCreatorEmailById(int id);

    PagingData<List<TransferRequestFlatDto>> getPermittedFlat(
            Integer requesterUserId,
            DataTableParams dataTableParams,
            VoucherFilterData voucherFilterData);

    String getHashById(Integer id);

}
