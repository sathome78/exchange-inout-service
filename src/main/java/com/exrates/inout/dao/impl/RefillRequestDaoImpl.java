package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.InvoiceConfirmData;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatAdditionalDataDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.exceptions.DuplicatedMerchantTransactionIdOrAttemptToRewriteException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.*;

import static com.exrates.inout.domain.enums.TransactionSourceType.REFILL;
import static java.util.Collections.singletonMap;
import static java.util.Optional.of;

@Repository
public class RefillRequestDaoImpl implements RefillRequestDao {

    private static final Logger log = LogManager.getLogger("refill");

    protected static RowMapper<RefillRequestFlatDto> refillRequestFlatDtoRowMapper = (rs, idx) -> {
        RefillRequestFlatDto refillRequestFlatDto = new RefillRequestFlatDto();
        refillRequestFlatDto.setId(rs.getInt("id"));
        refillRequestFlatDto.setAddress(rs.getString("address"));
        refillRequestFlatDto.setPrivKey(rs.getString("priv_key"));
        refillRequestFlatDto.setPubKey(rs.getString("pub_key"));
        refillRequestFlatDto.setBrainPrivKey(rs.getString("brain_priv_key"));
        refillRequestFlatDto.setUserId(rs.getInt("user_id"));
        refillRequestFlatDto.setPayerBankName(rs.getString("payer_bank_name"));
        refillRequestFlatDto.setPayerBankCode(rs.getString("payer_bank_code"));
        refillRequestFlatDto.setPayerAccount(rs.getString("payer_account"));
        refillRequestFlatDto.setRecipientBankAccount(rs.getString("payer_account"));
        refillRequestFlatDto.setUserFullName(rs.getString("user_full_name"));
        refillRequestFlatDto.setRemark(rs.getString("remark"), "");
        refillRequestFlatDto.setReceiptScan(rs.getString("receipt_scan"));
        refillRequestFlatDto.setReceiptScanName(rs.getString("receipt_scan_name"));
        refillRequestFlatDto.setAmount(rs.getBigDecimal("amount"));
        refillRequestFlatDto.setCommissionId(rs.getInt("commission_id"));
        refillRequestFlatDto.setStatus(RefillStatusEnum.convert(rs.getInt("status_id")));
        refillRequestFlatDto.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        refillRequestFlatDto.setStatusModificationDate(rs.getTimestamp("status_modification_date").toLocalDateTime());
        refillRequestFlatDto.setCurrencyId(rs.getInt("currency_id"));
        refillRequestFlatDto.setMerchantId(rs.getInt("merchant_id"));
        refillRequestFlatDto.setMerchantTransactionId(rs.getString("merchant_transaction_id"));
        refillRequestFlatDto.setRecipientBankId(rs.getInt("recipient_bank_id"));
        refillRequestFlatDto.setRecipientBankName(rs.getString("name"));
        refillRequestFlatDto.setRecipientBankAccount(rs.getString("account_number"));
        refillRequestFlatDto.setRecipientBankRecipient(rs.getString("recipient"));
        refillRequestFlatDto.setRecipientBankDetails(rs.getString("bank_details"));
        refillRequestFlatDto.setMerchantRequestSign(rs.getString("merchant_request_sign"));
        refillRequestFlatDto.setAdminHolderId(rs.getInt("admin_holder_id"));
        refillRequestFlatDto.setRefillRequestAddressId(rs.getInt("refill_request_address_id"));
        refillRequestFlatDto.setRefillRequestParamId(rs.getInt("refill_request_param_id"));
        return refillRequestFlatDto;
    };

    private static RowMapper<InvoiceBank> invoiceBankRowMapper = (rs, rowNum) -> {
        InvoiceBank bank = new InvoiceBank();
        bank.setId(rs.getInt("id"));
        bank.setName(rs.getString("name"));
        bank.setCurrencyId(rs.getInt("currency_id"));
        bank.setAccountNumber(rs.getString("account_number"));
        bank.setRecipient(rs.getString("recipient"));
        bank.setBankDetails(rs.getString("bank_details"));
        return bank;
    };

    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Integer> create(RefillRequestCreateDto request) {
        Optional<Integer> result = Optional.empty();
        if (request.getNeedToCreateRefillRequestRecord()) {
            final String sql = "INSERT INTO REFILL_REQUEST " +
                    " (amount, status_id, currency_id, user_id, commission_id, merchant_id, " +
                    "  date_creation, status_modification_date) " +
                    " VALUES " +
                    " (:amount, :status_id, :currency_id, :user_id, :commission_id, :merchant_id, " +
                    " NOW(), NOW())";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("amount", request.getAmount())
                    .addValue("status_id", request.getStatus().getCode())
                    .addValue("currency_id", request.getCurrencyId())
                    .addValue("user_id", request.getUserId())
                    .addValue("commission_id", request.getCommissionId())
                    .addValue("merchant_id", request.getMerchantId());
            namedParameterJdbcTemplate.update(sql, params, keyHolder);
            Integer refillRequestId = (int) keyHolder.getKey().longValue();
            request.setId(refillRequestId);
            result = of(refillRequestId);
            Integer refillRequestAddressId = null;
            Integer refillRequestParamId = null;
            if (!StringUtils.isEmpty(request.getAddress())) {
                Optional<Integer> addressIdResult = findAnyAddressIdByAddressAndUserAndCurrencyAndMerchant(request.getAddress(),
                        request.getUserId(),
                        request.getCurrencyId(),
                        request.getMerchantId());
                refillRequestAddressId = addressIdResult.orElseGet(() -> storeRefillRequestAddress(request));
            }
            refillRequestParamId = storeRefillRequestParam(request);
            final String setKeysSql = "UPDATE REFILL_REQUEST " +
                    " SET refill_request_param_id = :refill_request_param_id," +
                    "     refill_request_address_id = :refill_request_address_id, " +
                    "     remark = :remark" +
                    " WHERE id = :id ";
            params = new MapSqlParameterSource()
                    .addValue("id", refillRequestId)
                    .addValue("refill_request_param_id", refillRequestParamId)
                    .addValue("refill_request_address_id", refillRequestAddressId)
                    .addValue("remark", request.getRemark());
            namedParameterJdbcTemplate.update(setKeysSql, params);
        } else if (request.getStoreSameAddressForParentAndTokens() && isToken(request.getMerchantId())) {
            List<Map<String, Integer>> list = getTokenMerchants(request.getMerchantId());
            for (Map<String, Integer> record : list) {
                request.setMerchantId(record.get("merchantId"));
                request.setCurrencyId(record.get("currencyId"));
                storeRefillRequestAddress(request);
            }
        } else {
            storeRefillRequestAddress(request);
        }
        return result;
    }


    private Optional<Integer> findAnyAddressIdByAddressAndUserAndCurrencyAndMerchant(String address, Integer userId, Integer currencyId, Integer merchantId) {
        MapSqlParameterSource params;
        final String findAddressSql = "SELECT id " +
                " FROM REFILL_REQUEST_ADDRESS " +
                " WHERE currency_id = :currency_id AND merchant_id = :merchant_id AND user_id = :user_id AND address = :address " +
                " LIMIT 1 ";
        params = new MapSqlParameterSource()
                .addValue("currency_id", currencyId)
                .addValue("merchant_id", merchantId)
                .addValue("address", address)
                .addValue("user_id", userId);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(findAddressSql, params, Integer.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Integer storeRefillRequestParam(RefillRequestCreateDto request) {
        if (request.getRefillRequestParam().isEmpty()) {
            return null;
        }
        MapSqlParameterSource params;
        Integer refillRequestParamId;
        final String addParamSql = "INSERT INTO REFILL_REQUEST_PARAM " +
                " (id, recipient_bank_id, user_full_name, merchant_request_sign) " +
                " VALUES " +
                " (:id, :recipient_bank_id, :user_full_name, :merchant_request_sign) ";
        params = new MapSqlParameterSource()
                .addValue("id", request.getId())
                .addValue("recipient_bank_id", request.getRefillRequestParam().getRecipientBankId())
                .addValue("user_full_name", request.getRefillRequestParam().getUserFullName())
                .addValue("merchant_request_sign", request.getRefillRequestParam().getMerchantRequestSign());
        namedParameterJdbcTemplate.update(addParamSql, params);
        refillRequestParamId = request.getId();
        return refillRequestParamId;
    }

    private Integer storeRefillRequestAddress(RefillRequestCreateDto request) {
        MapSqlParameterSource params;
        Integer refillRequestAddressId;
        final String addAddressSql = "INSERT INTO REFILL_REQUEST_ADDRESS " +
                " (id, currency_id, merchant_id, address, user_id, priv_key, pub_key, brain_priv_key) " +
                " VALUES " +
                " (:id, :currency_id, :merchant_id, :address, :user_id, :priv_key, :pub_key, :brain_priv_key) ";
        params = new MapSqlParameterSource()
                .addValue("id", request.getId())
                .addValue("currency_id", request.getCurrencyId())
                .addValue("merchant_id", request.getMerchantId())
                .addValue("address", request.getAddress())
                .addValue("user_id", request.getUserId())
                .addValue("priv_key", request.getPrivKey())
                .addValue("pub_key", request.getPubKey())
                .addValue("brain_priv_key", request.getBrainPrivKey());
        namedParameterJdbcTemplate.update(addAddressSql, params);
        refillRequestAddressId = request.getId();
        return refillRequestAddressId;
    }

    public Optional<String> findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(
            Integer merchantId,
            Integer currencyId,
            Integer userId) {
        final String sql = "SELECT RRA.address " +
                " FROM REFILL_REQUEST_ADDRESS RRA " +
                " WHERE RRA.currency_id = :currency_id AND RRA.merchant_id = :merchant_id AND RRA.user_id = :user_id AND is_valid = 1" +
                " ORDER BY RRA.id DESC " +
                " LIMIT 1 ";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("currency_id", currencyId)
                .addValue("merchant_id", merchantId)
                .addValue("user_id", userId);
        try {
            return of(namedParameterJdbcTemplate.queryForObject(sql, params, String.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void setStatusById(Integer id, InvoiceStatus newStatus) {
        final String sql = "UPDATE REFILL_REQUEST " +
                "  SET status_id = :new_status_id, " +
                "      status_modification_date = NOW() " +
                "  WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("new_status_id", newStatus.getCode());
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setStatusAndConfirmationDataById(
            Integer id,
            InvoiceStatus newStatus,
            InvoiceConfirmData invoiceConfirmData) {
        final String sql = "UPDATE REFILL_REQUEST " +
                "  SET status_id = :new_status_id, " +
                "      status_modification_date = NOW(), " +
                "      remark = :remark " +
                "  WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("new_status_id", newStatus.getCode());
        params.put("remark", invoiceConfirmData.getRemark());
        namedParameterJdbcTemplate.update(sql, params);
        /**/
        final String updateParamSql = "UPDATE REFILL_REQUEST_PARAM " +
                "  JOIN REFILL_REQUEST ON (REFILL_REQUEST.refill_request_param_id = REFILL_REQUEST_PARAM.id) AND (REFILL_REQUEST.id = :id)" +
                "  SET payer_bank_code = :payer_bank_code, " +
                "      payer_bank_name = :payer_bank_name, " +
                "      payer_account = :payer_account, " +
                "      user_full_name = :user_full_name, " +
                "      receipt_scan_name = :receipt_scan_name, " +
                "      receipt_scan = :receipt_scan ";
        params = new HashMap<>();
        params.put("id", id);
        params.put("payer_bank_code", invoiceConfirmData.getPayerBankCode());
        params.put("payer_bank_name", invoiceConfirmData.getPayerBankName());
        params.put("payer_account", invoiceConfirmData.getUserAccount());
        params.put("user_full_name", invoiceConfirmData.getUserFullName());
        params.put("receipt_scan_name", invoiceConfirmData.getReceiptScanName());
        params.put("receipt_scan", invoiceConfirmData.getReceiptScanPath());
        namedParameterJdbcTemplate.update(updateParamSql, params);
    }

    public void setMerchantRequestSignById(
            Integer id,
            String sign) {
        MapSqlParameterSource params;
        String selectRefillSql = "SELECT refill_request_param_id FROM REFILL_REQUEST WHERE id = :id";
        params = new MapSqlParameterSource()
                .addValue("id", id);
        Integer refillRequestParamId = namedParameterJdbcTemplate.queryForObject(selectRefillSql, params, Integer.class);
        if (refillRequestParamId == null) {
            String addParamSql = "INSERT INTO REFILL_REQUEST_PARAM " +
                    " (id, merchant_request_sign) " +
                    " VALUES " +
                    " (:id, :merchant_request_sign) ";
            params = new MapSqlParameterSource()
                    .addValue("id", id)
                    .addValue("merchant_request_sign", sign);
            namedParameterJdbcTemplate.update(addParamSql, params);
            refillRequestParamId = id;
            String updateRefillSql = "UPDATE REFILL_REQUEST SET refill_request_param_id = :refill_request_param_id WHERE id = :id";
            params = new MapSqlParameterSource()
                    .addValue("refill_request_param_id", refillRequestParamId)
                    .addValue("id", id);
            namedParameterJdbcTemplate.update(updateRefillSql, params);
        } else {
            String updateParamSql = "UPDATE REFILL_REQUEST_PARAM " +
                    " SET merchant_request_sign = :merchant_request_sign " +
                    " WHERE id = :id";
            params = new MapSqlParameterSource()
                    .addValue("id", refillRequestParamId)
                    .addValue("merchant_request_sign", sign);
            namedParameterJdbcTemplate.update(updateParamSql, params);
        }
    }

    public List<InvoiceBank> findInvoiceBankListByCurrency(Integer currencyId) {
        final String sql = "SELECT id, currency_id, name, account_number, recipient, bank_details " +
                " FROM INVOICE_BANK " +
                " WHERE currency_id = :currency_id AND hidden = 0";
        final Map<String, Integer> params = Collections.singletonMap("currency_id", currencyId);
        return namedParameterJdbcTemplate.query(sql, params, invoiceBankRowMapper);
    }

    public Optional<InvoiceBank> findInvoiceBankById(Integer id) {
        final String sql = "SELECT id, currency_id, name, account_number, recipient, bank_details " +
                " FROM INVOICE_BANK " +
                " WHERE id = :id";
        final Map<String, Integer> params = Collections.singletonMap("id", id);
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, params, invoiceBankRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<RefillRequestFlatDto> getFlatByIdAndBlock(Integer id) {
        String sql = "SELECT  REFILL_REQUEST.*, RRA.*, RRP.*,  " +
                "                 INVOICE_BANK.name, INVOICE_BANK.account_number, INVOICE_BANK.recipient, INVOICE_BANK.bank_details " +
                " FROM REFILL_REQUEST " +
                "   LEFT JOIN REFILL_REQUEST_ADDRESS RRA ON (RRA.id = REFILL_REQUEST.refill_request_address_id)  " +
                "   LEFT JOIN REFILL_REQUEST_PARAM RRP ON (RRP.id = REFILL_REQUEST.refill_request_param_id) " +
                "   LEFT JOIN INVOICE_BANK ON (INVOICE_BANK.id = RRP.recipient_bank_id) " +
                " WHERE REFILL_REQUEST.id = :id " +
                " FOR UPDATE ";
        try {
            return of(namedParameterJdbcTemplate.queryForObject(sql, singletonMap("id", id), refillRequestFlatDtoRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<RefillRequestFlatDto> getFlatById(Integer id) {
        String sql = "SELECT  REFILL_REQUEST.*, RRA.*, RRP.*,  " +
                "                 INVOICE_BANK.name, INVOICE_BANK.account_number, INVOICE_BANK.recipient, INVOICE_BANK.bank_details " +
                " FROM REFILL_REQUEST " +
                "   LEFT JOIN REFILL_REQUEST_ADDRESS RRA ON (RRA.id = REFILL_REQUEST.refill_request_address_id)  " +
                "   LEFT JOIN REFILL_REQUEST_PARAM RRP ON (RRP.id = REFILL_REQUEST.refill_request_param_id) " +
                "   LEFT JOIN INVOICE_BANK ON (INVOICE_BANK.id = RRP.recipient_bank_id) " +
                " WHERE REFILL_REQUEST.id = :id ";
        try {
            return of(namedParameterJdbcTemplate.queryForObject(sql, singletonMap("id", id), refillRequestFlatDtoRowMapper));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public PagingData<List<RefillRequestFlatDto>> getPermittedFlatByStatus(
            List<Integer> statusIdList,
            Integer requesterUserId,
            DataTableParams dataTableParams,
            RefillFilterData refillFilterData) {
        final String JOINS_FOR_USER =
                " JOIN USER ON USER.id = REFILL_REQUEST.user_id ";
        String filter = refillFilterData.getSQLFilterClause();
        log.debug("filter clause {}", filter);
        String searchClause = dataTableParams.getSearchByEmailAndNickClause();
        String sqlBase =
                " FROM REFILL_REQUEST " +
                        " LEFT JOIN REFILL_REQUEST_ADDRESS RRA ON (RRA.id = REFILL_REQUEST.refill_request_address_id)  " +
                        " LEFT JOIN REFILL_REQUEST_PARAM RRP ON (RRP.id = REFILL_REQUEST.refill_request_param_id) " +
                        " LEFT JOIN INVOICE_BANK IB ON (IB.id = RRP.recipient_bank_id) " +
                        getPermissionClause(requesterUserId) +
                        JOINS_FOR_USER +
                        (statusIdList.isEmpty() ? "" : " WHERE status_id IN (:status_id_list) ");

        String whereClauseFilter = StringUtils.isEmpty(filter) ? "" : " AND ".concat(filter);
        String whereClauseSearch = StringUtils.isEmpty(searchClause) || !StringUtils.isEmpty(whereClauseFilter)
                ? "" : " AND ".concat(searchClause);
        String orderClause = dataTableParams.getOrderByClause();
        String offsetAndLimit = dataTableParams.getLimitAndOffsetClause();
        String sqlMain = String.join(" ", "SELECT REFILL_REQUEST.*, RRA.*, RRP.*, IB.*, IOP.invoice_operation_permission_id ",
                sqlBase, whereClauseFilter, whereClauseSearch, orderClause, offsetAndLimit);
        String sqlCount = String.join(" ", "SELECT COUNT(*) ", sqlBase, whereClauseFilter);
        Map<String, Object> params = new HashMap<>() {{
            put("status_id_list", statusIdList);
            put("requester_user_id", requesterUserId);
            put("operation_direction", "REFILL");
            put("offset", dataTableParams.getStart());
            put("limit", dataTableParams.getLength());
        }};
        params.putAll(refillFilterData.getNamedParams());
        params.putAll(dataTableParams.getSearchNamedParams());
        log.debug("sql {}", sqlMain);
        List<RefillRequestFlatDto> requests = namedParameterJdbcTemplate.query(sqlMain, params, (rs, i) -> {
            RefillRequestFlatDto refillRequestFlatDto = refillRequestFlatDtoRowMapper.mapRow(rs, i);
            refillRequestFlatDto.setInvoiceOperationPermission(InvoiceOperationPermission.convert(rs.getInt("invoice_operation_permission_id")));
            return refillRequestFlatDto;
        });
        Integer totalQuantity = namedParameterJdbcTemplate.queryForObject(sqlCount, params, Integer.class);
        PagingData<List<RefillRequestFlatDto>> result = new PagingData<>();
        result.setData(requests);
        result.setFiltered(totalQuantity);
        result.setTotal(totalQuantity);
        return result;
    }

    public RefillRequestFlatDto getPermittedFlatById(
            Integer id,
            Integer requesterUserId) {
        String sql = "SELECT  REFILL_REQUEST.*, RRA.*, RRP.*, " +
                "                 INVOICE_BANK.name, INVOICE_BANK.account_number, INVOICE_BANK.recipient, INVOICE_BANK.bank_details, " +
                "                 IOP.invoice_operation_permission_id " +
                " FROM REFILL_REQUEST " +
                "   LEFT JOIN REFILL_REQUEST_ADDRESS RRA ON (RRA.id = REFILL_REQUEST.refill_request_address_id) " +
                "   LEFT JOIN REFILL_REQUEST_PARAM RRP ON (RRP.id = REFILL_REQUEST.refill_request_param_id) " +
                "   LEFT JOIN INVOICE_BANK ON (INVOICE_BANK.id = RRP.recipient_bank_id) " +
                getPermissionClause(requesterUserId) +
                " WHERE REFILL_REQUEST.id=:id ";
        Map<String, Object> params = new HashMap<>() {{
            put("id", id);
            put("requester_user_id", requesterUserId);
            put("operation_direction", "REFILL");
        }};
        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, i) -> {
            RefillRequestFlatDto refillRequestFlatDto = refillRequestFlatDtoRowMapper.mapRow(rs, i);
            refillRequestFlatDto.setInvoiceOperationPermission(InvoiceOperationPermission.convert(rs.getInt("invoice_operation_permission_id")));
            return refillRequestFlatDto;
        });
    }

    public RefillRequestFlatAdditionalDataDto getAdditionalDataForId(int id) {
        String sql = "SELECT " +
                "   CUR.name AS currency_name, " +
                "   USER.email AS user_email, " +
                "   ADMIN.email AS admin_email, " +
                "   M.name AS merchant_name, " +
                "   TX.amount AS amount, TX.commission_amount AS commission_amount, " +
                "   (SELECT IF(MAX(confirmation_number) IS NULL, -1, MAX(confirmation_number)) FROM REFILL_REQUEST_CONFIRMATION RRC WHERE RRC.refill_request_id = :id) AS confirmations, " +
                "   (SELECT amount FROM REFILL_REQUEST_CONFIRMATION RRC WHERE RRC.refill_request_id = :id ORDER BY id DESC LIMIT 1) AS amount_by_bch " +
                " FROM REFILL_REQUEST RR " +
                " JOIN CURRENCY CUR ON (CUR.id = RR.currency_id) " +
                " JOIN USER USER ON (USER.id = RR.user_id) " +
                " LEFT JOIN USER ADMIN ON (ADMIN.id = RR.admin_holder_id) " +
                " JOIN MERCHANT M ON (M.id = RR.merchant_id) " +
                " LEFT JOIN TRANSACTION TX ON (TX.source_type = :source_type) AND (TX.source_id = :id) " +
                " WHERE RR.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("source_type", REFILL.name());
        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, idx) -> {
                    RefillRequestFlatAdditionalDataDto refillRequestFlatAdditionalDataDto = new RefillRequestFlatAdditionalDataDto();
                    refillRequestFlatAdditionalDataDto.setUserEmail(rs.getString("user_email"));
                    refillRequestFlatAdditionalDataDto.setAdminHolderEmail(rs.getString("admin_email"));
                    refillRequestFlatAdditionalDataDto.setCurrencyName(rs.getString("currency_name"));
                    refillRequestFlatAdditionalDataDto.setMerchantName(rs.getString("merchant_name"));
                    refillRequestFlatAdditionalDataDto.setCommissionAmount(rs.getBigDecimal("commission_amount"));
                    refillRequestFlatAdditionalDataDto.setTransactionAmount(rs.getBigDecimal("amount"));
                    refillRequestFlatAdditionalDataDto.setByBchAmount(rs.getBigDecimal("amount_by_bch"));
                    refillRequestFlatAdditionalDataDto.setConfirmations(rs.getInt("confirmations"));
                    return refillRequestFlatAdditionalDataDto;
                }
        );
    }

    public void setHolderById(Integer id, Integer holderId) {
        final String sql = "UPDATE REFILL_REQUEST " +
                "  SET admin_holder_id = :admin_holder_id " +
                "  WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("admin_holder_id", holderId);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setRemarkById(Integer id, String remark) {
        final String sql = "UPDATE REFILL_REQUEST " +
                "  SET remark = :remark " +
                "  WHERE id = :id ";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("remark", remark);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void setMerchantTransactionIdById(Integer id, String merchantTransactionId) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException {
        final String sql = "UPDATE REFILL_REQUEST RR" +
                "  LEFT JOIN REFILL_REQUEST RRI ON (RRI.id <> RR.id) " +
                "  AND (RRI.merchant_id = RR.merchant_id) AND (RRI.merchant_transaction_id = :merchant_transaction_id) " +
                "  AND (RR.refill_request_address_id IS NULL OR RRI.refill_request_address_id = RR.refill_request_address_id)" +
                "  SET RR.merchant_transaction_id = :merchant_transaction_id " +
                "  WHERE RR.id = :id AND RRI.id IS NULL ";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("merchant_transaction_id", merchantTransactionId);
        int result = namedParameterJdbcTemplate.update(sql, params);
        if (result == 0) {
            throw new DuplicatedMerchantTransactionIdOrAttemptToRewriteException(merchantTransactionId);
        }
    }

    public boolean checkInputRequests(int currencyId, String email) {
        String sql = "SELECT " +
                " (SELECT COUNT(*) FROM REFILL_REQUEST REQUEST " +
                " JOIN USER ON(USER.id = REQUEST.user_id) " +
                " WHERE USER.email = :email and REQUEST.currency_id = currency_id " +
                " and DATE(REQUEST.date_creation) = CURDATE()) <  " +
                " " +
                "(SELECT CURRENCY_LIMIT.max_daily_request FROM CURRENCY_LIMIT  " +
                " JOIN USER ON (USER.roleid = CURRENCY_LIMIT.user_role_id) " +
                " WHERE USER.email = :email AND operation_type_id = 1 AND currency_id = :currency_id) ;";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("currency_id", currencyId);
        params.put("email", email);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class) == 1;
    }

    public Optional<Integer> findUserIdById(Integer requestId) {
        String sql = "SELECT RR.user_id " +
                " FROM REFILL_REQUEST RR " +
                " WHERE RR.id = :id ";
        Map<String, Object> params = new HashMap<>() {{
            put("id", requestId);
        }};
        try {
            return Optional.of(namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private String getPermissionClause(Integer requesterUserId) {
        if (requesterUserId == null) {
            return " LEFT JOIN USER_CURRENCY_INVOICE_OPERATION_PERMISSION IOP ON (IOP.user_id = -1) ";
        }
        return " JOIN USER_CURRENCY_INVOICE_OPERATION_PERMISSION IOP ON " +
                "	  			(IOP.currency_id=REFILL_REQUEST.currency_id) " +
                "	  			AND (IOP.user_id=:requester_user_id) " +
                "	  			AND (IOP.operation_direction=:operation_direction) ";
    }


    public boolean isToken(Integer merchantId) {

        final String sql = "SELECT COUNT(id) FROM MERCHANT where (id = :merchant_id AND tokens_parrent_id is not null) " +
                "OR (tokens_parrent_id = :merchant_id)";
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, singletonMap("merchant_id", merchantId), Integer.class) > 0;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public List<Map<String, Integer>> getTokenMerchants(Integer merchantId) {

        final String sql = "SELECT merchant_id, currency_id FROM MERCHANT_CURRENCY where merchant_id" +
                " IN (SELECT id FROM (SELECT id FROM MERCHANT where id = :merchant_id OR tokens_parrent_id = :merchant_id" +
                " UNION" +
                " SELECT id FROM MERCHANT where MERCHANT.tokens_parrent_id IN (SELECT tokens_parrent_id FROM MERCHANT where id = :merchant_id)" +
                " OR MERCHANT.id IN (SELECT tokens_parrent_id FROM MERCHANT where id = :merchant_id)) as InnerQuery)";

        try {
            return namedParameterJdbcTemplate.query(sql, singletonMap("merchant_id", merchantId), (rs, row) -> {
                Map<String, Integer> map = new HashMap<>();
                map.put("merchantId", rs.getInt("merchant_id"));
                map.put("currencyId", rs.getInt("currency_id"));

                return map;
            });
        } catch (EmptyResultDataAccessException e) {
            Map<String, Integer> map = new HashMap<>();
            return new ArrayList((Collection) map);
        }
    }

    public Integer findMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId) {
        final String sql = "SELECT merchant_id FROM REFILL_REQUEST_ADDRESS RRA " +
                " WHERE RRA.address = :address AND currency_id = :currency_id AND user_id = :user_id ";
        Map<String, Object> map = new HashMap<>();
        map.put("address", address);
        map.put("currency_id", currencyId);
        map.put("user_id", userId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        } catch (Exception e) {
            return null;
        }
    }
}

