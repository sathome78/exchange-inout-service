package com.exrates.inout.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
public class MerchantCurrencyLifetimeDto  implements RowMapper<MerchantCurrencyLifetimeDto> {
    private Integer merchantId;
    private Integer currencyId;
    private Integer refillLifetimeHours;
    private Integer withdrawLifetimeHours;

    @Override
    public MerchantCurrencyLifetimeDto mapRow(ResultSet rs, int i) throws SQLException {

        MerchantCurrencyLifetimeDto result =  new MerchantCurrencyLifetimeDto();
        result.setCurrencyId(rs.getInt("currency_id"));
        result.setMerchantId(rs.getInt("merchant_id"));
        result.setRefillLifetimeHours(rs.getInt("refill_lifetime_hours"));
        return result;
    }
}
