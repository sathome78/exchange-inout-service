package com.exrates.inout.dao.rowmapper;

import com.exrates.inout.domain.dto.CurrencyPairTurnoverReportDto;
import com.exrates.inout.domain.enums.OperationType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyPairTurnoverReportDtoRowMapper implements RowMapper<CurrencyPairTurnoverReportDto> {
    @Override
    public CurrencyPairTurnoverReportDto mapRow(ResultSet rs, int i) throws SQLException {
        CurrencyPairTurnoverReportDto dto = new CurrencyPairTurnoverReportDto();
        dto.setOrderNum(i + 1);
        dto.setCurrencyPairName(rs.getString("currency_pair_name"));
        dto.setCurrencyAccountingName(rs.getString("currency_ac_name"));
        dto.setOperationType(OperationType.convert(rs.getInt("operation_type_id")));
        dto.setAmountBase(rs.getBigDecimal("amount_base"));
        dto.setAmountConvert(rs.getBigDecimal("amount_convert"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setPairId(rs.getInt("currency__pair_id"));
        return dto;
    }
}
