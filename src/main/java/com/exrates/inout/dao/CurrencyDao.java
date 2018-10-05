package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.CurrencyPairLimitDto;
import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.UserCurrencyOperationPermissionDto;
import com.exrates.inout.domain.enums.CurrencyPairType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.CurrencyPair;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CurrencyDao {

    List<Currency> getCurrList();

    int getCurrencyId(int walletId);

    String getCurrencyName(int currencyId);

    Currency findByName(String name);

    Currency findById(int id);

    BigDecimal retrieveMinLimitForRoleAndCurrency(UserRole userRole, OperationType operationType, Integer currencyId);

    List<UserCurrencyOperationPermissionDto> findCurrencyOperationPermittedByUserAndDirection(Integer userId, String operationDirection);

    List<String> getWarningForCurrency(Integer currencyId, UserCommentTopicEnum currencyWarningTopicEnum);

    List<String> getWarningsByTopic(UserCommentTopicEnum currencyWarningTopicEnum);

    List<String> getWarningForMerchant(Integer merchantId, UserCommentTopicEnum currencyWarningTopicEnum);

    MerchantCurrencyScaleDto findCurrencyScaleByCurrencyId(Integer currencyId);

    boolean isCurrencyIco(Integer currencyId);

    List<CurrencyPair> getAllCurrencyPairs(CurrencyPairType currencyPairType);

    CurrencyPair findCurrencyPairById(int currencyPair);

    Optional<Integer> findOpenCurrencyPairIdByName(String pairName);

    CurrencyPairLimitDto findCurrencyPairLimitForRoleByPairAndType(int currencyPairId, int role, int type);

    CurrencyPair findCurrencyPairByOrderId(int orderId);

    CurrencyPair findCurrencyPairByName(String pairName);
}