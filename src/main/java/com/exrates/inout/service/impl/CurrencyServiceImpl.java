package com.exrates.inout.service.impl;

import lombok.extern.log4j.Log4j2;
import me.exrates.dao.CurrencyDao;
import me.exrates.model.CurrencyLimit;
import me.exrates.model.CurrencyPair;
import me.exrates.model.User;
import me.exrates.model.dto.CurrencyPairLimitDto;
import me.exrates.model.dto.CurrencyReportInfoDto;
import me.exrates.model.dto.MerchantCurrencyScaleDto;
import me.exrates.model.dto.UserCurrencyOperationPermissionDto;
import me.exrates.model.dto.mobileApiDto.TransferLimitDto;
import me.exrates.model.dto.mobileApiDto.dashboard.CurrencyPairWithLimitsDto;
import me.exrates.model.dto.openAPI.CurrencyPairInfoItem;
import me.exrates.model.enums.*;
import me.exrates.model.enums.invoice.InvoiceOperationDirection;
import me.exrates.service.CurrencyService;
import me.exrates.service.UserRoleService;
import me.exrates.service.UserService;
import me.exrates.service.api.ExchangeApi;
import me.exrates.service.exception.CurrencyPairNotFoundException;
import me.exrates.service.exception.ScaleForAmountNotSetException;
import me.exrates.service.util.BigDecimalConverter;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.Objects.isNull;

/**
 * @author Denis Savin (pilgrimm333@gmail.com)
 */
@Log4j2
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyDao currencyDao;

    @Autowired
    private UserService userService;

    @Autowired
    UserRoleService userRoleService;

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
    public List<Currency> getAllActiveCurrencies() {
        return currencyDao.getAllActiveCurrencies();
    }

    @Transactional(transactionManager = "slaveTxManager", readOnly = true)
    @Override
    public List<Currency> getAllCurrencies() {
        return currencyDao.getAllActiveCurrencies();
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
    public List<Currency> findAllCurrencies() {
        return currencyDao.findAllCurrencies();
    }

    @Override
    public void updateCurrencyLimit(int currencyId, OperationType operationType, String roleName, BigDecimal minAmount, BigDecimal minAmountUSD, Integer maxDailyRequest) {
        currencyDao.updateCurrencyLimit(currencyId, operationType, userRoleService.getRealUserRoleIdByBusinessRoleList(roleName), minAmount, minAmountUSD, maxDailyRequest);
    }

    @Override
    public void updateCurrencyLimit(int currencyId, OperationType operationType, BigDecimal minAmount, BigDecimal minAmountUSD, Integer maxDailyRequest) {

        currencyDao.updateCurrencyLimit(currencyId, operationType, minAmount, minAmountUSD, maxDailyRequest);
    }

    @Override
    public List<CurrencyLimit> retrieveCurrencyLimitsForRole(String roleName, OperationType operationType) {
        return currencyDao.retrieveCurrencyLimitsForRoles(
                userRoleService.getRealUserRoleIdByBusinessRoleList(roleName),
                operationType);
    }

    @Override
    public BigDecimal retrieveMinLimitForRoleAndCurrency(UserRole userRole, OperationType operationType, Integer currencyId) {
        return currencyDao.retrieveMinLimitForRoleAndCurrency(userRole, operationType, currencyId);
    }

    @Override
    public List<CurrencyPair> getAllCurrencyPairs(CurrencyPairType type) {
        return currencyDao.getAllCurrencyPairs(type);
    }

    @Override
    public List<CurrencyPair> getAllCurrencyPairsWithHidden(CurrencyPairType type) {
        return currencyDao.getAllCurrencyPairs(type);
    }

    @Override
    public List<CurrencyPair> getAllCurrencyPairsInAlphabeticOrder(CurrencyPairType type) {
        List<CurrencyPair> result = currencyDao.getAllCurrencyPairs(type);
        result.sort(Comparator.comparing(CurrencyPair::getName));
        return result;
    }

    @Override
    public CurrencyPair findCurrencyPairById(int currencyPairId) {
        try {
            return currencyDao.findCurrencyPairById(currencyPairId);
        } catch (EmptyResultDataAccessException e) {
            throw new CurrencyPairNotFoundException("Currency pair not found");
        }
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
    public List<TransferLimitDto> retrieveMinTransferLimits(List<Integer> currencyIds) {
        Integer roleId = userService.getUserRoleFromSecurityContext().getRole();
        return currencyDao.retrieveMinTransferLimits(currencyIds, roleId);
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

    @Override
    @Transactional(readOnly = true)
    public Set<String> getCurrencyPermittedNameList(String userEmail) {
        Integer userId = userService.getIdByEmail(userEmail);
        return getCurrencyPermittedNameList(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserCurrencyOperationPermissionDto> getCurrencyPermittedOperationList(Integer userId) {
        return currencyDao.findCurrencyOperationPermittedByUserList(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<String> getCurrencyPermittedNameList(Integer userId) {
        return currencyDao.findCurrencyOperationPermittedByUserList(userId).stream()
                .map(UserCurrencyOperationPermissionDto::getCurrencyName)
                .collect(Collectors.toSet());
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
    public CurrencyPairLimitDto findLimitForRoleByCurrencyPairAndType(Integer currencyPairId, OperationType operationType) {
        UserRole userRole = userService.getUserRoleFromSecurityContext();
        OrderType orderType = OrderType.convert(operationType.name());
        return currencyDao.findCurrencyPairLimitForRoleByPairAndType(currencyPairId, userRole.getRole(), orderType.getType());
    }

    @Override
    public CurrencyPairLimitDto findLimitForRoleByCurrencyPairAndTypeAndUser(Integer currencyPairId, OperationType operationType, User user) {
        UserRole userRole = user.getRole();
        OrderType orderType = OrderType.convert(operationType.name());
        return currencyDao.findCurrencyPairLimitForRoleByPairAndType(currencyPairId, userRole.getRole(), orderType.getType());

    }

    @Override
    public List<CurrencyPairLimitDto> findAllCurrencyLimitsForRoleAndType(String roleName, OrderType orderType) {
        return currencyDao.findLimitsForRolesByType(userRoleService.getRealUserRoleIdByBusinessRoleList(roleName), orderType.getType());
    }

    @Override
    public void updateCurrencyPairLimit(Integer currencyPairId, OrderType orderType, String roleName, BigDecimal minRate, BigDecimal maxRate, BigDecimal minAmount, BigDecimal maxAmount) {
        currencyDao.setCurrencyPairLimit(currencyPairId, userRoleService.getRealUserRoleIdByBusinessRoleList(roleName), orderType.getType(), minRate,
                maxRate, minAmount, maxAmount);
    }

    @Override
    public List<CurrencyPairWithLimitsDto> findCurrencyPairsWithLimitsForUser() {
        Integer userRoleId = userService.getUserRoleFromSecurityContext().getRole();
        return currencyDao.findAllCurrencyPairsWithLimits(userRoleId);
    }

    @Override
    public List<Currency> findAllCurrenciesWithHidden() {
        return currencyDao.findAllCurrenciesWithHidden();
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
    @Transactional
    public MerchantCurrencyScaleDto getCurrencyScaleByCurrencyId(Integer currencyId) {
        MerchantCurrencyScaleDto result = currencyDao.findCurrencyScaleByCurrencyId(currencyId);
        Optional.ofNullable(result.getScaleForRefill()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        Optional.ofNullable(result.getScaleForWithdraw()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        return result;
    }

    @Override
    public CurrencyPair getCurrencyPairByName(String currencyPair) {
        return currencyDao.findCurrencyPairByName(currencyPair);
    }

    @Override
    public Integer findCurrencyPairIdByName(String pairName) {
        return currencyDao.findOpenCurrencyPairIdByName(pairName).orElseThrow(() -> new CurrencyPairNotFoundException(pairName));
    }

    @Override
    public List<Currency> findAllCurrenciesByProcessType(MerchantProcessType processType) {
        return currencyDao.findAllCurrenciesByProcessType(processType);
    }

    @Override
    public List<CurrencyPair> findPermitedCurrencyPairs(CurrencyPairType currencyPairType) {
        return currencyDao.findPermitedCurrencyPairs(currencyPairType);
    }

    @Override
    public CurrencyPair getNotHiddenCurrencyPairByName(String currencyPair) {
        return currencyDao.getNotHiddenCurrencyPairByName(currencyPair);
    }


    @Override
    public List<CurrencyPairInfoItem> findActiveCurrencyPairs() {
        return currencyDao.findActiveCurrencyPairs();
    }

    @Override
    public List<Currency> findAllCurrency() {
        return currencyDao.findAllCurrency();
    }

    @Override
    public boolean updateVisibilityCurrencyById(int currencyId) {
        return currencyDao.updateVisibilityCurrencyById(currencyId);
    }

    @Override
    public List<CurrencyPair> findAllCurrencyPair() {
        return currencyDao.findAllCurrencyPair();
    }

    @Override
    public boolean updateVisibilityCurrencyPairById(int currencyPairId) {
        return currencyDao.updateVisibilityCurrencyPairById(currencyPairId);
    }

    @Override
    public boolean updateAccessToDirectLinkCurrencyPairById(int currencyPairId) {
        return currencyDao.updateAccessToDirectLinkCurrencyPairById(currencyPairId);
    }

    @Override
    public List<CurrencyReportInfoDto> getStatsByCoin(int currencyId) {
        return currencyDao.getStatsByCoin(currencyId);
    }

    @Override
    public boolean setPropertyCalculateLimitToUsd(int currencyId, OperationType operationType, String roleName, Boolean recalculateToUsd) {
        return currencyDao.setPropertyCalculateLimitToUsd(currencyId, operationType, userRoleService.getRealUserRoleIdByBusinessRoleList(roleName), recalculateToUsd);
    }

    @Override
    public void updateWithdrawLimits() {
        StopWatch stopWatch = StopWatch.createStarted();
        log.info("Process of updating withdraw limits start...");

        List<CurrencyLimit> currencyLimits = currencyDao.getAllCurrencyLimits();

        final Map<String, Pair<BigDecimal, BigDecimal>> rates = exchangeApi.getRates();

        if (rates.isEmpty()) {
            log.info("Exchange api did not return data");
            return;
        }

        for (CurrencyLimit currencyLimit : currencyLimits) {
            final String currencyName = currencyLimit.getCurrency().getName();
            final boolean recalculateToUsd = currencyLimit.isRecalculateToUsd();
            BigDecimal minSumUsdRate = currencyLimit.getMinSumUsdRate();
            BigDecimal minSum = currencyLimit.getMinSum();

            Pair<BigDecimal, BigDecimal> pairRates = rates.get(currencyName);

            if (isNull(pairRates)) {
                continue;
            }
            final BigDecimal usdRate = pairRates.getLeft();
            currencyLimit.setCurrencyUsdRate(usdRate);

            if (recalculateToUsd) {
                minSum = BigDecimalConverter.convert(minSumUsdRate.divide(usdRate, RoundingMode.HALF_UP));
                currencyLimit.setMinSum(minSum);
            } else {
                minSumUsdRate = minSum.multiply(usdRate);
                currencyLimit.setMinSumUsdRate(minSumUsdRate);
            }
        }
        currencyDao.updateWithdrawLimits(currencyLimits);

        log.info("Process of updating withdraw limits end... Time: {}", stopWatch.getTime(TimeUnit.MILLISECONDS));
    }

    @Override
    public List<Currency> getCurrencies(MerchantProcessType... processType) {
        return currencyDao.getCurrencies(processType);
    }

    @Override
    public List<CurrencyPair> getPairsByFirstPartName(String partName) {
        return currencyDao.findAllCurrenciesByFirstPartName(partName);
    }

    @Override
    public List<CurrencyPair> getPairsBySecondPartName(String partName) {
        return currencyDao.findAllCurrenciesBySecondPartName(partName);
    }
}
