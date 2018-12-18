package com.exrates.inout.service;


import com.exrates.inout.domain.dto.MerchantCurrencyApiDto;
import com.exrates.inout.domain.dto.MerchantCurrencyBasicInfoDto;
import com.exrates.inout.domain.dto.MerchantCurrencyLifetimeDto;
import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.TransferMerchantApiDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.MerchantCurrency;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public interface MerchantService {

    List<Merchant> findAll();

    Merchant findById(int id);

    Merchant findByName(String name);

    List<MerchantCurrency> getAllUnblockedForOperationTypeByCurrencies(List<Integer> currenciesId, OperationType operationType);

    List<MerchantCurrencyApiDto> findNonTransferMerchantCurrencies(Integer currencyId);

    Optional<MerchantCurrency> findByMerchantAndCurrency(int merchantId, int currencyId);

    List<TransferMerchantApiDto> findTransferMerchants();

    BigDecimal getMinSum(Integer merchantId, Integer currencyId);

    void checkAmountForMinSum(Integer merchantId, Integer currencyId, BigDecimal amount);

    MerchantCurrencyLifetimeDto getMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);

    MerchantCurrencyScaleDto getMerchantCurrencyScaleByMerchantIdAndCurrencyId(Integer merchantId, Integer currencyId);

    void checkMerchantIsBlocked(Integer merchantId, Integer currencyId, OperationType operationType);

    Optional<String> getCoreWalletPassword(String merchantName, String currencyName);

    /*pass file format : classpath: merchants/pass/<merchant>_pass.properties
     * stored values: wallet.password
     *                node.bitcoind.rpc.user
     *                node.bitcoind.rpc.password
     * */
    @SneakyThrows
    Properties getPassMerchantProperties(String merchantName);

    Map<String, String> computeCommissionAndMapAllToString(BigDecimal amount,
                                                           OperationType type,
                                                           String currency,
                                                           String merchant);

    void checkDestinationTag(Integer merchantId, String memo);

    boolean isValidDestinationAddress(Integer merchantId, String address);

    List<String> getWarningsForMerchant(OperationType operationType, Integer merchantId, Locale locale);

    List<Integer> getIdsByProcessType(List<String> processType);

    boolean getSubtractFeeFromAmount(Integer merchantId, Integer currencyId);

    void setSubtractFeeFromAmount(Integer merchantId, Integer currencyId, boolean subtractFeeFromAmount);

    List<MerchantCurrencyBasicInfoDto> findTokenMerchantsByParentId(Integer parentId);

    @Transactional
    List<MerchantCurrencyLifetimeDto> getMerchantCurrencyWithRefillLifetime();
}
