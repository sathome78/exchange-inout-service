package com.exrates.inout.domain.dto;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class MerchantSpecParamDto implements RowMapper<MerchantSpecParamDto> {

    private int id;
    private int merchantId;
    private String paramName;
    private String paramValue;

    @Override
    public MerchantSpecParamDto mapRow(ResultSet rs, int i) throws SQLException {
        MerchantSpecParamDto dto = new MerchantSpecParamDto();
        dto.setId(rs.getInt("id"));
        dto.setMerchantId(rs.getInt("merchant_id"));
        dto.setParamName(rs.getString("param_name"));
        dto.setParamValue(rs.getString("param_value"));
        return dto;
    }
}
