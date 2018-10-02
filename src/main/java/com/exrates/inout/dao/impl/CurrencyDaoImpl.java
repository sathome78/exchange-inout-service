package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.CurrencyDao;
import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.UserCurrencyOperationPermissionDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.main.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CurrencyDaoImpl implements CurrencyDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CurrencyDaoImpl(@Qualifier(value = "masterTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Currency> getCurrList() {
        String sql = "SELECT id, name FROM CURRENCY WHERE hidden IS NOT TRUE ";
        List<Currency> currList;
        currList = jdbcTemplate.query(sql, (rs, row) -> {
            Currency currency = new Currency();
            currency.setId(rs.getInt("id"));
            currency.setName(rs.getString("name"));
            return currency;

        });
        return currList;
    }

    public int getCurrencyId(int walletId) {
        String sql = "SELECT currency_id FROM WALLET WHERE id = :walletId ";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("walletId", String.valueOf(walletId));
        return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
    }

    public String getCurrencyName(int currencyId) {
        String sql = "SELECT name FROM CURRENCY WHERE  id = :id ";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("id", String.valueOf(currencyId));
        return jdbcTemplate.queryForObject(sql, namedParameters, String.class);
    }

    public Currency findByName(String name) {
        final String sql = "SELECT * FROM CURRENCY WHERE name = :name";
        final Map<String, String> params = new HashMap<>() {
            {
                put("name", name);
            }
        };
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Currency.class));
    }

    public Currency findById(int id) {
        final String sql = "SELECT * FROM CURRENCY WHERE id = :id";
        final Map<String, Integer> params = new HashMap<>() {
            {
                put("id", id);
            }
        };
        return jdbcTemplate.queryForObject(sql, params, new BeanPropertyRowMapper<>(Currency.class));
    }

    public BigDecimal retrieveMinLimitForRoleAndCurrency(UserRole userRole, OperationType operationType, Integer currencyId) {
        String sql = "SELECT min_sum FROM CURRENCY_LIMIT " +
                "WHERE user_role_id = :role_id AND operation_type_id = :operation_type_id AND currency_id = :currency_id";
        Map<String, Integer> params = new HashMap<>() {{
            put("role_id", userRole.getRole());
            put("operation_type_id", operationType.getType());
            put("currency_id", currencyId);
        }};
        return jdbcTemplate.queryForObject(sql, params, BigDecimal.class);
    }

    public List<UserCurrencyOperationPermissionDto> findCurrencyOperationPermittedByUserAndDirection(Integer userId, String operationDirection) {
        String sql = "SELECT CUR.id, CUR.name, IOP.invoice_operation_permission_id" +
                " FROM CURRENCY CUR " +
                " LEFT JOIN USER_CURRENCY_INVOICE_OPERATION_PERMISSION IOP ON " +
                "				(IOP.currency_id=CUR.id) " +
                "			 	AND (IOP.operation_direction=:operation_direction) " +
                "				AND (IOP.user_id=:user_id) " +
                " WHERE CUR.hidden IS NOT TRUE " +
                " ORDER BY CUR.id ";
        Map<String, Object> params = new HashMap<>() {{
            put("user_id", userId);
            put("operation_direction", operationDirection);
        }};
        return jdbcTemplate.query(sql, params, (rs, row) -> {
            UserCurrencyOperationPermissionDto dto = new UserCurrencyOperationPermissionDto();
            dto.setUserId(userId);
            dto.setCurrencyId(rs.getInt("id"));
            dto.setCurrencyName(rs.getString("name"));
            Integer permissionCode = rs.getObject("invoice_operation_permission_id") == null ? 0 : (Integer) rs.getObject("invoice_operation_permission_id");
            dto.setInvoiceOperationPermission(InvoiceOperationPermission.convert(permissionCode));
            return dto;
        });
    }


    public List<String> getWarningForCurrency(Integer currencyId, UserCommentTopicEnum currencyWarningTopicEnum) {
        String sql = "SELECT PHT.template " +
                " FROM PHRASE_TEMPLATE PHT " +
                " JOIN USER_COMMENT_TOPIC UCT ON (UCT.id = PHT.topic_id) AND (UCT.topic = :topic)  " +
                " JOIN CURRENCY CUR ON (CUR.id = :currency_id)" +
                " WHERE PHT.template LIKE CONCAT('%.', CUR.name) ";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id", currencyId);
        params.put("topic", currencyWarningTopicEnum.name());
        return jdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<String> getWarningsByTopic(UserCommentTopicEnum currencyWarningTopicEnum) {
        String sql = "SELECT PHT.template " +
                " FROM PHRASE_TEMPLATE PHT " +
                " JOIN USER_COMMENT_TOPIC UCT ON (UCT.id = PHT.topic_id) AND (UCT.topic = :topic)  ";
        Map<String, Object> params = new HashMap<>();
        params.put("topic", currencyWarningTopicEnum.name());
        return jdbcTemplate.queryForList(sql, params, String.class);
    }

    public List<String> getWarningForMerchant(Integer merchantId, UserCommentTopicEnum currencyWarningTopicEnum) {
        String sql = "SELECT PHT.template " +
                " FROM PHRASE_TEMPLATE PHT " +
                " JOIN USER_COMMENT_TOPIC UCT ON (UCT.id = PHT.topic_id) AND (UCT.topic = :topic)  " +
                " JOIN MERCHANT MCH ON (MCH.id = :merchant_id)" +
                " WHERE PHT.template LIKE CONCAT('%.', REPLACE(MCH.name, ' ', '')) ";
        Map<String, Object> params = new HashMap<>();
        params.put("merchant_id", merchantId);
        params.put("topic", currencyWarningTopicEnum.name());
        return jdbcTemplate.queryForList(sql, params, String.class);
    }


    public MerchantCurrencyScaleDto findCurrencyScaleByCurrencyId(Integer currencyId) {
        String sql = "SELECT id, max_scale_for_refill, max_scale_for_withdraw FROM CURRENCY WHERE id = :currency_id";
        Map<String, Object> params = new HashMap<>() {{
            put("currency_id", currencyId);
        }};
        return jdbcTemplate.queryForObject(sql, params, (rs, i) -> {
            MerchantCurrencyScaleDto result = new MerchantCurrencyScaleDto();
            result.setCurrencyId(rs.getInt("id"));
            result.setMerchantId(null);
            result.setScaleForRefill((Integer) rs.getObject("max_scale_for_refill"));
            result.setScaleForWithdraw((Integer) rs.getObject("max_scale_for_withdraw"));
            return result;
        });
    }

    public boolean isCurrencyIco(Integer currencyId) {
        String sql = "SELECT id FROM CURRENCY_PAIR WHERE hidden IS NOT TRUE AND type = 'ICO' AND currency1_id =:currency_id";
        return !jdbcTemplate.queryForList(sql, Collections.singletonMap("currency_id", currencyId), Integer.class).isEmpty();
    }

}