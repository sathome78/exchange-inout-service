package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.EthereumNodeDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EthereumNodeDaoImpl implements EthereumNodeDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final Logger LOG = LogManager.getLogger("node_ethereum");

    public EthereumNodeDaoImpl(@Qualifier(value = "masterTemplate") final NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String> findAllAddresses(String merchant) {
        final String sql = "SELECT ETHEREUM_TEMP_ACCOUNT.address FROM ETHEREUM_TEMP_ACCOUNT " +
                "where merchant_id = (select id from MERCHANT where name = :merchant)";

        final Map<String, String> params = new HashMap<>();
        params.put("merchant", merchant);

        return jdbcTemplate.query(sql, params, (rs, row) -> rs.getString("address"));
    }
}
