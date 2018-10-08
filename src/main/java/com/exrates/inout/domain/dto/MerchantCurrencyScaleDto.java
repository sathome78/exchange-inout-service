package com.exrates.inout.domain.dto;


import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class MerchantCurrencyScaleDto implements RowMapper<MerchantCurrencyScaleDto> {
    private Integer merchantId;
    private Integer currencyId;
    private Integer scaleForRefill;
    private Integer scaleForWithdraw;
    private Integer scaleForTransfer;

    @Override
    public MerchantCurrencyScaleDto mapRow(ResultSet rs, int i) throws SQLException {

        MerchantCurrencyScaleDto result = new MerchantCurrencyScaleDto();
        result.setCurrencyId(rs.getInt("id"));
        result.setMerchantId(null);
        result.setScaleForRefill((Integer) rs.getObject("max_scale_for_refill"));
        result.setScaleForWithdraw((Integer) rs.getObject("max_scale_for_withdraw"));
        return result;
    }
}
