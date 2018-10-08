package com.exrates.inout.domain.dto;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class CurrencyPairLimitDto implements RowMapper<CurrencyPairLimitDto> {
  private Integer currencyPairId;
  private String currencyPairName;
  private BigDecimal minRate;
  private BigDecimal maxRate;
  private BigDecimal minAmount;
  private BigDecimal maxAmount;

  @Override
  public CurrencyPairLimitDto mapRow(ResultSet rs, int i) throws SQLException {
    CurrencyPairLimitDto dto = new CurrencyPairLimitDto();
    dto.setCurrencyPairId(rs.getInt("currency_pair_id"));
    dto.setCurrencyPairName(rs.getString("currency_pair_name"));
    dto.setMinRate(rs.getBigDecimal("min_rate"));
    dto.setMaxRate(rs.getBigDecimal("max_rate"));
    dto.setMinAmount(rs.getBigDecimal("min_amount"));
    dto.setMaxAmount(rs.getBigDecimal("max_amount"));
    return dto;
  }
}
