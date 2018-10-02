package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.InputOutputDao;
import com.exrates.inout.domain.dto.MyInputOutputHistoryDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.other.PaginationWrapper;
import com.exrates.inout.util.BigDecimalProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InputOutputDaoImpl implements InputOutputDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public InputOutputDaoImpl(@Qualifier(value = "masterTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PaginationWrapper<List<MyInputOutputHistoryDto>> findUnconfirmedInvoices(Integer userId, Integer currencyId, @Nullable Integer limit, @Nullable Integer offset) {
        String limitSql = "";
        String offsetSql = "";
        Map<String, Object> params = new HashMap<>();
        if (limit != null) {
            limitSql = " LIMIT :limit ";
            params.put("limit", limit);
            if (offset != null) {
                offsetSql = " OFFSET :offset";
                params.put("offset", offset);
            }
        }
        String sql = "SELECT RR.id AS request_id, RR.date_creation AS datetime, CUR.name AS currency, RR.amount, COM.value AS commission_value," +
                " MER.name AS merchant, RR.user_id, IB.account_number AS destination, RR.status_id, RR.status_modification_date," +
                " RRP.user_full_name, RR.remark, RR.admin_holder_id, RR.merchant_transaction_id AS transaction_hash" +
                " FROM REFILL_REQUEST RR " +
                " JOIN CURRENCY CUR ON RR.currency_id = CUR.id" +
                " JOIN MERCHANT MER ON RR.merchant_id = MER.id" +
                " JOIN COMMISSION COM ON RR.commission_id = COM.id" +
                " LEFT JOIN REFILL_REQUEST_PARAM RRP ON RR.refill_request_param_id = RRP.id" +
                " LEFT JOIN INVOICE_BANK IB ON RRP.recipient_bank_id = IB.id " +
                " WHERE RR.user_id = :user_id AND RR.currency_id = :currency_id AND RR.status_id IN (:status_ids) " +
                " ORDER BY datetime DESC "
                + limitSql + offsetSql;
        String sqlCount = "SELECT COUNT(*) " +
                " FROM REFILL_REQUEST RR " +
                " JOIN CURRENCY CUR ON RR.currency_id = CUR.id" +
                " JOIN MERCHANT MER ON RR.merchant_id = MER.id" +
                " JOIN COMMISSION COM ON RR.commission_id = COM.id" +
                " WHERE RR.user_id = :user_id AND RR.currency_id = :currency_id AND RR.status_id IN (:status_ids) ";

        params.put("user_id", userId);
        params.put("currency_id", currencyId);
        params.put("status_ids", Arrays.asList(RefillStatusEnum.WAITING_CONFIRMATION_USER.getCode(), RefillStatusEnum.DECLINED_ADMIN.getCode()));

        Integer count = jdbcTemplate.queryForObject(sqlCount, params, Integer.class);
        List<MyInputOutputHistoryDto> result = jdbcTemplate.query(sql, params, (rs, i) -> {
            MyInputOutputHistoryDto myInputOutputHistoryDto = new MyInputOutputHistoryDto();
            myInputOutputHistoryDto.setId(rs.getInt("request_id"));
            Timestamp datetime = rs.getTimestamp("datetime");
            myInputOutputHistoryDto.setDatetime(datetime == null ? null : datetime.toLocalDateTime());
            myInputOutputHistoryDto.setCurrencyName(rs.getString("currency"));
            BigDecimal amount = rs.getBigDecimal("amount");
            BigDecimal commissionValue = rs.getBigDecimal("commission_value");
            BigDecimal commissionAmount = BigDecimalProcessing.doAction(amount, commissionValue, ActionType.MULTIPLY_PERCENT);
            myInputOutputHistoryDto.setAmount(BigDecimalProcessing.formatNonePoint(amount, 2));
            myInputOutputHistoryDto.setCommissionAmount(BigDecimalProcessing.formatNonePoint(commissionAmount, 2));
            myInputOutputHistoryDto.setMerchantName(rs.getString("merchant"));
            myInputOutputHistoryDto.setOperationType(OperationType.INPUT.name());
            myInputOutputHistoryDto.setUserId(rs.getInt("user_id"));
            myInputOutputHistoryDto.setBankAccount(rs.getString("destination"));
            myInputOutputHistoryDto.setSourceType(TransactionSourceType.REFILL);
            myInputOutputHistoryDto.setStatus(rs.getInt("status_id"));
            Timestamp dateModification = rs.getTimestamp("status_modification_date");
            myInputOutputHistoryDto.setStatusUpdateDate(dateModification == null ? null : dateModification.toLocalDateTime());
            myInputOutputHistoryDto.setUserFullName(rs.getString("user_full_name"));
            myInputOutputHistoryDto.setRemark(rs.getString("remark"));
            myInputOutputHistoryDto.setAdminHolderId(rs.getInt("admin_holder_id"));
            myInputOutputHistoryDto.setTransactionHash(rs.getString("transaction_hash"));
            return myInputOutputHistoryDto;
        });

        return new PaginationWrapper<>(result, count, limit == null ? 0 : limit);

    }

}
