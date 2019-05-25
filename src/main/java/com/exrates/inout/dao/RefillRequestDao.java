package com.exrates.inout.dao;

import com.exrates.inout.domain.RefillOnConfirmationDto;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.domain.main.RefillRequestAddressShortDto;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RefillRequestDao {

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    Optional<Integer> findIdWithoutConfirmationsByAddressAndMerchantIdAndCurrencyIdAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    Optional<Integer> findIdByAddressAndMerchantIdAndCurrencyIdAndHash(String address, Integer merchantId, Integer currencyId, String hash);

    Optional<Integer> findIdByMerchantIdAndCurrencyIdAndHash(
            Integer merchantId,
            Integer currencyId,
            String hash);

    Optional<RefillRequestFlatDto> findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address, Integer merchantId,
            Integer currencyId,
            String hash);

    List<RefillRequestFlatDto> findAllWithoutConfirmationsByMerchantIdAndCurrencyIdAndStatusId(Integer merchantId, Integer currencyId, List<Integer> statusList);

    List<RefillRequestFlatDto> findAllWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(Integer merchantId, Integer currencyId, List<Integer> statusIdList);

    Integer getCountByMerchantIdAndCurrencyIdAndAddressAndStatusId(String address, Integer merchantId, Integer currencyId, List<Integer> statusList);

    Optional<Integer> findUserIdByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId);

    Optional<Integer> create(RefillRequestCreateDto request);

    Integer storeRefillRequestAddress(RefillRequestCreateDto request);

    Optional<String> findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId);

    List<String> getListOfValidAddressByMerchantIdAndCurrency(
            Integer merchantId,
            Integer currencyId);

    void setStatusById(Integer id, InvoiceStatus newStatus);

    void setStatusAndConfirmationDataById(Integer id, InvoiceStatus newStatus, InvoiceConfirmData invoiceConfirmData);

    void setMerchantRequestSignById(Integer id, String sign);

    List<InvoiceBank> findInvoiceBankListByCurrency(Integer currencyId);

    Optional<InvoiceBank> findInvoiceBankById(Integer id);

    Optional<LocalDateTime> getAndBlockByIntervalAndStatus(Integer merchantId, Integer currencyId, Integer intervalHours, List<Integer> statusIdList);

    Optional<RefillRequestFlatDto> getFlatByIdAndBlock(Integer id);

    Optional<RefillRequestFlatDto> getFlatById(Integer id);

    void setNewStatusByDateIntervalAndStatus(Integer merchantId, Integer currencyId, LocalDateTime boundDate, Integer intervalHours, Integer newStatusId, List<Integer> statusIdList);

    List<OperationUserDto> findListByMerchantIdAndCurrencyIdStatusChangedAtDate(Integer merchantId, Integer currencyId, Integer statusId, LocalDateTime dateWhenChanged);

    PagingData<List<RefillRequestFlatDto>> getPermittedFlatByStatus(List<Integer> statusIdList, Integer requesterUserId, DataTableParams dataTableParams, RefillFilterData refillFilterData);

    RefillRequestFlatDto getPermittedFlatById(Integer id, Integer requesterUserId);

    RefillRequestFlatAdditionalDataDto getAdditionalDataForId(int id);

    void setHolderById(Integer id, Integer holderId);

    void setRemarkById(Integer id, String remark);

    void setMerchantTransactionIdById(Integer id, String merchantTransactionId) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException;

    boolean checkInputRequests(int currencyId, UserRole userRole, int userId);

    void setConfirmationsNumberByRequestId(Integer requestId, BigDecimal amount, Integer confirmations, String blockhash);

    Optional<Integer> findUserIdById(Integer requestId);

    Optional<RefillRequestBtcInfoDto> findRefillRequestByAddressAndMerchantTransactionId(String address,
                                                                                         String merchantTransactionId,
                                                                                         Integer merchantId,
                                                                                         Integer currencyId);

    Optional<String> getLastBlockHashForMerchantAndCurrency(Integer merchantId, Integer currencyId);


    List<String> findAllAddresses(Integer merchantId, Integer currencyId, List<Boolean> isValidStatuses);

    List<RefillRequestFlatDto> findAllNotAcceptedByAddressAndMerchantAndCurrency(String address, Integer merchantId, Integer currencyId);

    int getTxOffsetForAddress(String address);

    void updateTxOffsetForAddress(String address, Integer offset);

    boolean isToken(Integer merchantId);

    List<Map<String, Integer>> getTokenMerchants(Integer merchantId);

    Integer findMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId);

    void updateAddressNeedTransfer(String address, Integer merchantId, Integer currencyId, boolean isNeeded);

    void invalidateAddress(String address, Integer merchantId, Integer currencyId);

    List<RefillRequestAddressDto> findAllAddressesNeededToTransfer(Integer merchantId, Integer currencyId);

    List<RefillRequestAddressDto> findByAddressMerchantAndCurrency(String address, Integer merchantId, Integer currencyId);

    List<RefillRequestAddressDto> findAddressDtosByMerchantAndCurrency(Integer merchantId, Integer currencyId);

    PagingData<List<RefillRequestAddressShortDto>> getAddresses(DataTableParams dataTableParams, RefillAddressFilterData data);

    List<Integer> getUnconfirmedTxsCurrencyIdsForTokens(int parentTokenId);

    List<RefillRequestFlatDto> findAllWithChildTokensWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(int merchantId, int currencyId, List<Integer> collect);

    List<RefillRequestAddressDto> findByAddress(String address);

    String getUsernameByAddressAndCurrencyIdAndMerchantId(String address, int currencyId, int merchantId);

    String getGaTagByRequestId(int requestId);

    boolean setAddressBlocked(String address, int merchantId, int currencyId, boolean blocked);

    List<RefillRequestAddressShortDto> getBlockedAddresses(int merchantId, int currencyId);

    void setInnerTransferHash(int requestId, String hash);

    List<RefillRequestAddressDto> findAllAddressesByMerchantWithChilds(int merchantId);

    List<RefillOnConfirmationDto> getOnConfirmationDtos(Integer userId, int currencyId);

    RefillRequestAddressDto findByAddressAndMerchantIdAndCurrencyIdAndUserId(String address, int merchantId, int currencyId, int userId);

    Optional<Integer> autoCreate(RefillRequestAcceptDto request, int userId, int commissionId, RefillStatusEnum statusEnum);

    Optional<RefillRequestBtcInfoDto> findRefillRequestByAddressAndMerchantIdAndCurrencyIdAndTransactionId(int merchantId, int currencyId, String txHash);

    String getPrivKeyByAddress(String address);
}
