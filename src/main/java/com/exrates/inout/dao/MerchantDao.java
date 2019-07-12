package com.exrates.inout.dao;


import com.exrates.inout.domain.dto.MerchantCurrencyApiDto;
import com.exrates.inout.domain.dto.MerchantCurrencyAutoParamDto;
import com.exrates.inout.domain.dto.MerchantCurrencyBasicInfoDto;
import com.exrates.inout.domain.dto.MerchantCurrencyLifetimeDto;
import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.TransferMerchantApiDto;
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

    List<Merchant> findAll();

    List<Merchant> findAllByCurrency(int currencyId);

    BigDecimal getMinSum(int merchant, int currency);

    Optional<MerchantCurrency> findByMerchantAndCurrency(int merchantId, int currencyId);

    List<MerchantCurrency> findAllUnblockedForOperationTypeByCurrencies(List<Integer> currenciesId, OperationType operationType);

    List<MerchantCurrencyApiDto> findAllMerchantCurrencies(Integer currencyId, UserRole userRole, List<String> merchantProcessTypes);

    List<TransferMerchantApiDto> findTransferMerchants();

    List<Integer> findCurrenciesIdsByType(List<String> processTypes);

    boolean checkMerchantBlock(Integer merchantId, Integer currencyId, OperationType operationType);

    MerchantCurrencyAutoParamDto findAutoWithdrawParamsByMerchantAndCurrency(Integer merchantId, Integer currencyId);

    List<MerchantCurrencyLifetimeDto> findMerchantCurrencyWithRefillLifetime();

    MerchantCurrencyLifetimeDto findMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);

    MerchantCurrencyScaleDto findMerchantCurrencyScaleByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);

    boolean getSubtractFeeFromAmount(Integer merchantId, Integer currencyId);

    void setSubtractFeeFromAmount(Integer merchantId, Integer currencyId, boolean subtractFeeFromAmount);

    List<MerchantCurrencyBasicInfoDto> findTokenMerchantsByParentId(Integer parentId);

    void setAutoWithdrawParamsByMerchantAndCurrency(Integer merchantId, Integer currencyId, Boolean withdrawAutoEnabled, Integer withdrawAutoDelaySeconds, BigDecimal withdrawAutoThresholdAmount);

    void setMinSum(double merchantId, double currencyId, double minSum);
}