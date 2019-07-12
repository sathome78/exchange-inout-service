package com.exrates.inout.dao.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.MerchantSpecParamDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


//@Log4j2
@Repository
public class MerchantSpecParamsDaoImpl implements MerchantSpecParamsDao {

   private static final Logger log = LogManager.getLogger(MerchantSpecParamsDaoImpl.class);


    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public MerchantSpecParamDto getByMerchantNameAndParamName(String merchantName, String paramName) {
        String sql = " SELECT MSP.* FROM MERCHANT_SPEC_PARAMETERS MSP " +
                " INNER JOIN MERCHANT M ON M.id = MSP.merchant_id " +
                " WHERE M.name = :merchant_name AND MSP.param_name = :param_name ";
        Map<String, Object> params = new HashMap<>();
        params.put("merchant_name", merchantName);
        params.put("param_name", paramName);
        try {
            return jdbcTemplate.queryForObject(sql, params, new MerchantSpecParamDto());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public MerchantSpecParamDto getByMerchantIdAndParamName(int merchantId, String paramName) {
        String sql = " SELECT MSP.* FROM MERCHANT_SPEC_PARAMETERS MSP " +
                " INNER JOIN MERCHANT M ON M.id = MSP.merchant_id " +
                " WHERE M.id = :merchant_id AND MSP.param_name = :param_name ";
        Map<String, Object> params = new HashMap<>();
        params.put("merchant_id", merchantId);
        params.put("param_name", paramName);
        try {
            return jdbcTemplate.queryForObject(sql, params, new MerchantSpecParamDto());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public boolean updateParam(String merchantName, String paramName, String newValue) {
        String sql = " UPDATE MERCHANT_SPEC_PARAMETERS MSP " +
                " INNER JOIN MERCHANT M ON M.id = MSP.merchant_id " +
                " SET MSP.param_value = :new_value " +
                " WHERE M.name = :merchant_name AND MSP.param_name = :param_name ";
        Map<String, Object> params = new HashMap<>();
        params.put("merchant_name", merchantName);
        params.put("param_name", paramName);
        params.put("new_value", newValue);
        return jdbcTemplate.update(sql, params) > 0;
    }
}
