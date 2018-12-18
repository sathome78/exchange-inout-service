package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.CommissionDao;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Commission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;

@Repository
public class CommissionDaoImpl implements CommissionDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Commission> commissionRowMapper = (resultSet, i) -> {
        Commission commission = new Commission();
        commission.setDateOfChange(resultSet.getDate("date"));
        commission.setId(resultSet.getInt("id"));
        commission.setOperationType(OperationType.convert(resultSet.getInt("operation_type")));
        commission.setValue(resultSet.getBigDecimal("value"));
        return commission;
    };

    @Autowired
    public CommissionDaoImpl(@Qualifier(value = "masterTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Commission getCommission(OperationType operationType, UserRole userRole) {
        final String sql = "SELECT COMMISSION.id, COMMISSION.operation_type, COMMISSION.date, COMMISSION.value " +
                "FROM COMMISSION " +
                "WHERE operation_type = :operation_type AND user_role = :role_id";
        final HashMap<String, Integer> params = new HashMap<>();
        params.put("operation_type", operationType.type);
        params.put("role_id", userRole.getRole());
        return jdbcTemplate.queryForObject(sql, params, commissionRowMapper);
    }

    public Commission getCommission(OperationType operationType, Integer userId) {
        final String sql = "SELECT COMMISSION.id, COMMISSION.operation_type, COMMISSION.date, COMMISSION.value " +
                "FROM COMMISSION " +
                "JOIN USER ON USER.id = :user_id " +
                "WHERE operation_type = :operation_type AND user_role = USER.roleid";
        final HashMap<String, Integer> params = new HashMap<>();
        params.put("operation_type", operationType.type);
        params.put("user_id", userId);
        return jdbcTemplate.queryForObject(sql, params, commissionRowMapper);
    }

    public BigDecimal getCommissionMerchant(String merchant, String currency, OperationType operationType) {
        String selectedField = resolveCommissionDataField(operationType);
        final String sql = "SELECT " + selectedField + " FROM birzha.MERCHANT_CURRENCY " +
                "where merchant_id = (select id from MERCHANT where name = :merchant) \n" +
                "and currency_id = (select id from CURRENCY where name = :currency)";
        final HashMap<String, String> params = new HashMap<>();
        params.put("currency", currency);
        params.put("merchant", merchant);
        return BigDecimal.valueOf(jdbcTemplate.queryForObject(sql, params, Double.class));
    }

    public BigDecimal getCommissionMerchant(Integer merchantId, Integer currencyId, OperationType operationType) {
        String selectedField = resolveCommissionDataField(operationType);
        final String sql = "SELECT " + selectedField + " FROM MERCHANT_CURRENCY " +
                "where merchant_id = :merchant_id " +
                "and currency_id = :currency_id ";
        final HashMap<String, Object> params = new HashMap<>();
        params.put("currency_id", currencyId);
        params.put("merchant_id", merchantId);
        return BigDecimal.valueOf(jdbcTemplate.queryForObject(sql, params, Double.class));
    }

    private String resolveCommissionDataField(OperationType operationType) {
        String selectedField;
        switch (operationType) {
            case INPUT: {
                selectedField = "merchant_input_commission";
                break;
            }
            case OUTPUT: {
                selectedField = "merchant_output_commission";
                break;
            }
            case USER_TRANSFER: {
                selectedField = "merchant_transfer_commission";
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid operation type: " + operationType);
            }
        }
        return selectedField;
    }

    public BigDecimal getMinFixedCommission(Integer currencyId, Integer merchantId) {
        final String sql = "SELECT merchant_fixed_commission FROM MERCHANT_CURRENCY " +
                "where merchant_id = :merchant " +
                "and currency_id = :currency ";
        final HashMap<String, Object> params = new HashMap<>();
        params.put("currency", currencyId);
        params.put("merchant", merchantId);
        return BigDecimal.valueOf(jdbcTemplate.queryForObject(sql, params, Double.class));
    }

    public Commission getCommissionById(Integer commissionId) {
        final String sql = "SELECT COMMISSION.id, COMMISSION.operation_type, COMMISSION.date, COMMISSION.value " +
                " FROM COMMISSION " +
                " WHERE id = :id";
        final HashMap<String, Integer> params = new HashMap<>();
        params.put("id", commissionId);
        return jdbcTemplate.queryForObject(sql, params, commissionRowMapper);
    }

    @Override
    public Commission getDefaultCommission(OperationType operationType) {
        final String sql = "SELECT id, operation_type, date, value " +
                "FROM COMMISSION " +
                "WHERE operation_type = :operation_type AND user_role = 4;";
        final HashMap<String,Integer> params = new HashMap<>();
        params.put("operation_type",operationType.type);
        return jdbcTemplate.queryForObject(sql,params,commissionRowMapper);
    }
}
