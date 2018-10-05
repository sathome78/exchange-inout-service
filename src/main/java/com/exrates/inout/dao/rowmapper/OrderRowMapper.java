package com.exrates.inout.dao.rowmapper;

import com.exrates.inout.domain.OrderExtractor;
import com.exrates.inout.domain.main.ExOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<ExOrder> {

    public ExOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderExtractor orderExtractor = new OrderExtractor();
        return orderExtractor.extractData(rs);
    }
}
