package com.exrates.inout.domain;

import com.exrates.inout.domain.dto.ReportGroupUserRole;
import com.exrates.inout.domain.dto.UserGroupBalanceDto;
import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class UserGroupBalanceDtoRowMapper implements RowMapper<UserGroupBalanceDto> {
    @Override
    public UserGroupBalanceDto mapRow(ResultSet rs, int i) throws SQLException {
        UserGroupBalanceDto dto = new UserGroupBalanceDto();
        dto.setCurId(rs.getInt("currency_id"));
        dto.setCurrency(rs.getString("currency_name"));
        dto.setReportGroupUserRole(ReportGroupUserRole.valueOf(rs.getString("feature_name")));
        dto.setTotalBalance(rs.getBigDecimal("total_balance"));
        return dto;
    }
}
