package com.exrates.inout.domain;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.main.ExOrder;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class OrderExtractor implements ResultSetExtractor<ExOrder> {
    public ExOrder extractData(ResultSet rs) throws SQLException, DataAccessException {
        ExOrder exOrder = new ExOrder();
        exOrder.setId(rs.getInt("id"));
        exOrder.setUserId(rs.getInt("user_id"));
        exOrder.setCurrencyPairId(rs.getInt("currency_pair_id"));
        exOrder.setOperationType(OperationType.convert(rs.getInt("operation_type_id")));
        exOrder.setExRate(rs.getBigDecimal("exrate"));
        exOrder.setAmountBase(rs.getBigDecimal("amount_base"));
        exOrder.setComissionId(rs.getInt("commission_id"));
        exOrder.setAmountConvert(rs.getBigDecimal("amount_convert"));
        exOrder.setCommissionFixedAmount(rs.getBigDecimal("commission_fixed_amount"));
        exOrder.setUserAcceptorId(rs.getInt("user_acceptor_id"));
        exOrder.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        LocalDateTime dateAcception = LocalDateTime.MIN;
        if (rs.getTimestamp("date_acception") != null) {
            dateAcception = rs.getTimestamp("date_acception").toLocalDateTime();
        }
        exOrder.setDateAcception(dateAcception);
        exOrder.setStatus(OrderStatus.convert(rs.getInt("status_id")));
        return exOrder;
    }
}
