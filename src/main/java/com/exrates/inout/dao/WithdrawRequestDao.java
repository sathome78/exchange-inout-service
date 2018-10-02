package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.ClientBank;
import com.exrates.inout.domain.main.PagingData;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WithdrawRequestDao {

    int create(WithdrawRequestCreateDto withdrawRequest);

    void setStatusById(Integer id, InvoiceStatus newStatus);

    void setHashAndParamsById(Integer id, Map<String, String> hash);

    Optional<WithdrawRequestFlatDto> getFlatByIdAndBlock(int id);

    Optional<WithdrawRequestFlatDto> getFlatById(int id);

    PagingData<List<WithdrawRequestFlatDto>> getPermittedFlatByStatus(List<Integer> statusIdList, Integer requesterUserId, DataTableParams dataTableParams, WithdrawFilterData withdrawFilterData);

    WithdrawRequestFlatDto getPermittedFlatById(Integer id, Integer requesterUserId);

    void setHolderById(Integer id, Integer holderId);

    List<ClientBank> findClientBanksForCurrency(Integer currencyId);

    boolean checkOutputRequests(int currencyId, String email);

    Optional<Integer> findUserIdById(Integer requestId);

    List<Integer> getWithdrawalStatistic(String startDate, String endDate);

    WithdrawRequestInfoDto findWithdrawInfo(Integer id);

    WithdrawRequestFlatAdditionalDataDto getAdditionalDataForId(int id);
}
