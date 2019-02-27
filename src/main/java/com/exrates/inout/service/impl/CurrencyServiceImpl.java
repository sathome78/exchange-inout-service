package com.exrates.inout.service.impl;

import com.exrates.inout.dao.CurrencyDao;
import com.exrates.inout.domain.dto.MerchantCurrencyScaleDto;
import com.exrates.inout.domain.dto.UserCurrencyOperationPermissionDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.exceptions.ScaleForAmountNotSetException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.api.ExchangeApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.math.BigDecimal.ROUND_HALF_UP;


@Slf4j
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeApi exchangeApi;

    private static final Set<String> CRYPTO = new HashSet<String>() {
        {
            add("EDRC");
            add("BTC");
            add("LTC");
            add("EDR");
            add("ETH");
            add("ETC");
            add("DASH");
        }
    };
    private static final int CRYPTO_PRECISION = 8;
    private static final int DEFAULT_PRECISION = 2;
    private static final int EDC_OUTPUT_PRECISION = 3;

    @Override
    @Transactional(readOnly = true)
    public String getCurrencyName(int currencyId) {
        return currencyDao.getCurrencyName(currencyId);
    }

    @Override
    public Currency findByName(String name) {
        return currencyDao.findByName(name);
    }

    @Override
    public Currency findById(int id) {
        return currencyDao.findById(id);
    }

    @Override
    public BigDecimal retrieveMinLimitForRoleAndCurrency(UserRole userRole, OperationType operationType, Integer currencyId) {
        return currencyDao.retrieveMinLimitForRoleAndCurrency(userRole, operationType, currencyId);
    }

    @Override
    public String amountToString(final BigDecimal amount, final String currency) {
        return amount.setScale(resolvePrecision(currency), ROUND_HALF_UP)
                .toPlainString();
    }

    @Override
    public int resolvePrecision(final String currency) {
        return CRYPTO.contains(currency) ? CRYPTO_PRECISION : DEFAULT_PRECISION;
    }

    @Override
    public int resolvePrecisionByOperationType(final String currency, OperationType operationType) {

        return currency.equals(currencyDao.findByName("EDR").getName()) && (operationType == OperationType.OUTPUT) ?
                EDC_OUTPUT_PRECISION :
                CRYPTO.contains(currency) ? CRYPTO_PRECISION :
                        DEFAULT_PRECISION;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCurrencyOperationPermissionDto> findWithOperationPermissionByUserAndDirection(Integer userId, InvoiceOperationDirection operationDirection) {
        return currencyDao.findCurrencyOperationPermittedByUserAndDirection(userId, operationDirection.name());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCurrencyOperationPermissionDto> getCurrencyOperationPermittedForRefill(String userEmail) {
        return getCurrencyOperationPermittedList(userEmail, InvoiceOperationDirection.REFILL);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCurrencyOperationPermissionDto> getCurrencyOperationPermittedForWithdraw(String userEmail) {
        return getCurrencyOperationPermittedList(userEmail, InvoiceOperationDirection.WITHDRAW);
    }

    private List<UserCurrencyOperationPermissionDto> getCurrencyOperationPermittedList(String userEmail, InvoiceOperationDirection direction) {
        Integer userId = userService.getIdByEmail(userEmail);
        return findWithOperationPermissionByUserAndDirection(userId, direction);
    }

    @Override
    public List<String> getWarningForCurrency(Integer currencyId, UserCommentTopicEnum currencyWarningTopicEnum) {
        return currencyDao.getWarningForCurrency(currencyId, currencyWarningTopicEnum);
    }

    @Override
    public List<String> getWarningsByTopic(UserCommentTopicEnum currencyWarningTopicEnum) {
        return currencyDao.getWarningsByTopic(currencyWarningTopicEnum);
    }

    @Override
    public List<String> getWarningForMerchant(Integer merchantId, UserCommentTopicEnum currencyWarningTopicEnum) {
        return currencyDao.getWarningForMerchant(merchantId, currencyWarningTopicEnum);
    }

    @Override
    @Transactional(readOnly = true)
    public Currency getById(int id) {
        return currencyDao.findById(id);
    }

    @Override
    public BigDecimal computeRandomizedAddition(Integer currencyId, OperationType operationType) {
        Optional<OperationType.AdditionalRandomAmountParam> randomAmountParam = operationType.getRandomAmountParam(currencyId);
        if (!randomAmountParam.isPresent()) {
            return BigDecimal.ZERO;
        } else {
            OperationType.AdditionalRandomAmountParam param = randomAmountParam.get();
            return BigDecimal.valueOf(Math.random() * (param.highBound - param.lowBound) + param.lowBound).setScale(0, BigDecimal.ROUND_DOWN);
        }
    }

    @Override
    public boolean isIco(Integer currencyId) {
        return currencyDao.isCurrencyIco(currencyId);
    }

    @Override
    public List<Currency> getAllActiveCurrencies() {
        return currencyDao.getAllActiveCurrencies();
    }

    @Override
    @Transactional
    public MerchantCurrencyScaleDto getCurrencyScaleByCurrencyId(Integer currencyId) {
        MerchantCurrencyScaleDto result = currencyDao.findCurrencyScaleByCurrencyId(currencyId);
        Optional.ofNullable(result.getScaleForRefill()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        Optional.ofNullable(result.getScaleForWithdraw()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        return result;
    }

}
