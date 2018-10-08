package com.exrates.inout.domain.dto;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
public class MerchantCurrencyOptionsDto implements RowMapper<MerchantCurrencyOptionsDto> {
    private Integer merchantId;
    private Integer currencyId;
    private String merchantName;
    private String currencyName;
    private BigDecimal inputCommission;
    private BigDecimal outputCommission;
    private BigDecimal transferCommission;
    private BigDecimal minFixedCommission;
    private Boolean isRefillBlocked;
    private Boolean isWithdrawBlocked;
    private Boolean isTransferBlocked;
    private Boolean withdrawAutoEnabled;
    private Integer withdrawAutoDelaySeconds;
    private BigDecimal withdrawAutoThresholdAmount;
    private Boolean isMerchantCommissionSubtractedForWithdraw;

    @Override
    public MerchantCurrencyOptionsDto mapRow(ResultSet rs, int i) throws SQLException {

        MerchantCurrencyOptionsDto dto = new MerchantCurrencyOptionsDto();
        dto.setMerchantId(rs.getInt("merchant_id"));
        dto.setCurrencyId(rs.getInt("currency_id"));
        dto.setMerchantName(rs.getString("merchant_name"));
        dto.setCurrencyName(rs.getString("currency_name"));
        dto.setInputCommission(rs.getBigDecimal("merchant_input_commission"));
        dto.setOutputCommission(rs.getBigDecimal("merchant_output_commission"));
        dto.setTransferCommission(rs.getBigDecimal("merchant_transfer_commission"));
        dto.setIsRefillBlocked(rs.getBoolean("refill_block"));
        dto.setIsWithdrawBlocked(rs.getBoolean("withdraw_block"));
        dto.setIsTransferBlocked(rs.getBoolean("transfer_block"));
        dto.setMinFixedCommission(rs.getBigDecimal("merchant_fixed_commission"));
        dto.setWithdrawAutoEnabled(rs.getBoolean("withdraw_auto_enabled"));
        dto.setWithdrawAutoDelaySeconds(rs.getInt("withdraw_auto_delay_seconds"));
        dto.setWithdrawAutoThresholdAmount(rs.getBigDecimal("withdraw_auto_threshold_amount"));
        dto.setIsMerchantCommissionSubtractedForWithdraw(rs.getBoolean("subtract_merchant_commission_for_withdraw"));
        return dto;
    }
}
