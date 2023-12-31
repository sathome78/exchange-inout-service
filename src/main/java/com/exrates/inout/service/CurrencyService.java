package com.exrates.inout.service;

import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.UserCurrencyOperationPermissionDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.main.Currency;

import java.math.BigDecimal;
import java.util.List;

public interface CurrencyService {

    String getCurrencyName(int currencyId);

    Currency findByName(String name);

    Currency findById(int id);

    BigDecimal retrieveMinLimitForRoleAndCurrency(UserRole userRole, OperationType operationType, Integer currencyId);

    String amountToString(BigDecimal amount, String currency);

    int resolvePrecision(String currency);

    int resolvePrecisionByOperationType(final String currency, OperationType operationType);

    List<UserCurrencyOperationPermissionDto> getCurrencyOperationPermittedForRefill(String userEmail);

    List<UserCurrencyOperationPermissionDto> getCurrencyOperationPermittedForWithdraw(String userEmail);

    List<UserCurrencyOperationPermissionDto> findWithOperationPermissionByUserAndDirection(Integer userId, InvoiceOperationDirection operationDirection);

    List<String> getWarningForCurrency(Integer currencyId, UserCommentTopicEnum currencyWarningTopicEnum);

    List<String> getWarningsByTopic(UserCommentTopicEnum currencyWarningTopicEnum);

    List<String> getWarningForMerchant(Integer merchantId, UserCommentTopicEnum currencyWarningTopicEnum);

    Currency getById(int id);

    BigDecimal computeRandomizedAddition(Integer currencyId, OperationType operationType);

    MerchantCurrencyScaleDto getCurrencyScaleByCurrencyId(Integer currencyId);

    boolean isIco(Integer currencyId);

    List<Currency> getAllActiveCurrencies();
}
