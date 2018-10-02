package com.exrates.inout.dao;


import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.MerchantCurrency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MerchantDao {

    Merchant create(Merchant merchant);

    Merchant findById(int id);

    Merchant findByName(String name);

    BigDecimal getMinSum(int merchant, int currency);

    Optional<MerchantCurrency> findByMerchantAndCurrency(int merchantId, int currencyId);

    List<MerchantCurrency> findAllUnblockedForOperationTypeByCurrencies(List<Integer> currenciesId, OperationType operationType);

    List<MerchantCurrencyApiDto> findAllMerchantCurrencies(Integer currencyId, UserRole userRole, List<String> merchantProcessTypes);

    List<TransferMerchantApiDto> findTransferMerchants();

    List<Integer> findCurrenciesIdsByType(List<String> processTypes);

    boolean checkMerchantBlock(Integer merchantId, Integer currencyId, OperationType operationType);

    MerchantCurrencyAutoParamDto findAutoWithdrawParamsByMerchantAndCurrency(Integer merchantId, Integer currencyId);

    MerchantCurrencyLifetimeDto findMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);

    MerchantCurrencyScaleDto findMerchantCurrencyScaleByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);
}