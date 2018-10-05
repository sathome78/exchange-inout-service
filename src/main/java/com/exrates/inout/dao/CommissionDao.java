package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Commission;

import java.math.BigDecimal;

public interface CommissionDao {

    Commission getCommission(OperationType operationType, UserRole userRole);

    Commission getCommission(OperationType operationType, Integer userId);

    BigDecimal getCommissionMerchant(String merchant, String currency, OperationType operationType);

    BigDecimal getCommissionMerchant(Integer merchantId, Integer currencyId, OperationType operationType);

    BigDecimal getMinFixedCommission(Integer currencyId, Integer merchantId);

    Commission getCommissionById(Integer commissionId);

    Commission getDefaultCommission(OperationType storno);
}

