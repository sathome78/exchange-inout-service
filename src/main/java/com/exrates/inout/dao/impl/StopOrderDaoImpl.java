package com.exrates.inout.dao.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.StopOrderDao;
import com.exrates.inout.domain.StopOrder;
import com.exrates.inout.domain.enums.OrderStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by maks on 20.04.2017.
 */
@Repository
//@Log4j2
public class StopOrderDaoImpl implements StopOrderDao {

   private static final Logger log = LogManager.getLogger(StopOrderDaoImpl.class);


    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer create(StopOrder order) {
        String sql = "INSERT INTO STOP_ORDERS" +
                "  (user_id, currency_pair_id, operation_type_id, stop_rate,  limit_rate, amount_base, amount_convert, commission_id, commission_fixed_amount, status_id)" +
                "  VALUES " +
                "  (:user_id, :currency_pair_id, :operation_type_id, :stop_rate, :limit_rate, :amount_base, :amount_convert, :commission_id, :commission_fixed_amount, :status_id)";
        log.debug(sql);
        log.debug(order);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", order.getUserId())
                .addValue("currency_pair_id", order.getCurrencyPairId())
                .addValue("operation_type_id", order.getOperationType().getType())
                .addValue("stop_rate", order.getStop())
                .addValue("limit_rate", order.getLimit())
                .addValue("amount_base", order.getAmountBase())
                .addValue("amount_convert", order.getAmountConvert())
                .addValue("commission_id", order.getComissionId())
                .addValue("commission_fixed_amount", order.getCommissionFixedAmount())
                .addValue("status_id", OrderStatus.INPROCESS.getStatus());
        int result = namedParameterJdbcTemplate.update(sql, parameters, keyHolder);
        int id = (int) keyHolder.getKey().longValue();
        if (result <= 0) {
            id = 0;
        }
        return id;
    }

    @Override
    public boolean setStatus(int orderId, OrderStatus status) {
        String sql = "UPDATE STOP_ORDERS SET status_id=:status_id WHERE id = :id";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("status_id", String.valueOf(status.getStatus()));
        namedParameters.put("id", String.valueOf(orderId));
        int result = namedParameterJdbcTemplate.update(sql, namedParameters);
        return result > 0;
    }
}
