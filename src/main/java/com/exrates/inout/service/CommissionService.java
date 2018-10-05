package com.exrates.inout.service;


import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Commission;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

public interface CommissionService {

    Commission findCommissionByTypeAndRole(OperationType operationType, UserRole userRole);

    BigDecimal getCommissionMerchant(String merchant, String currency, OperationType operationType);

    BigDecimal getCommissionMerchant(Integer merchantId, Integer currencyId, OperationType operationType);

    BigDecimal getMinFixedCommission(Integer currencyId, Integer merchantId);

    Map<String, String> computeCommissionAndMapAllToString(Integer userId, BigDecimal amount, OperationType operationType, Integer currencyId, Integer merchantId, Locale locale, String destinationTag);

    CommissionDataDto normalizeAmountAndCalculateCommission(Integer userId, BigDecimal amount, OperationType type, Integer currencyId, Integer merchantId, String destinationTag);

    BigDecimal calculateCommissionForRefillAmount(BigDecimal amount, Integer commissionId);

    Commission getDefaultCommission(OperationType referral);
}
