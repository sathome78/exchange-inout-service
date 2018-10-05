package com.exrates.inout.service.impl;

import com.exrates.inout.dao.CommissionDao;
import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.enums.MerchantProcessType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.Commission;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.IllegalOperationTypeException;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.exrates.inout.domain.enums.ActionType.*;
import static com.exrates.inout.domain.enums.OperationType.*;
import static java.math.BigDecimal.*;

@Service
public class CommissionServiceImpl implements CommissionService {

    private final CommissionDao commissionDao;

    private final UserService userService;

    private final MerchantService merchantService;

    private final CurrencyService currencyService;

    private final MerchantServiceContext merchantServiceContext;

    private final MessageSource messageSource;

    @Autowired
    public CommissionServiceImpl(CommissionDao commissionDao, UserService userService,  MerchantService merchantService, CurrencyService currencyService, MerchantServiceContext merchantServiceContext, MessageSource messageSource) {
        this.commissionDao = commissionDao;
        this.userService = userService;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
        this.merchantServiceContext = merchantServiceContext;
        this.messageSource = messageSource;
    }

    public Commission findCommissionByTypeAndRole(OperationType operationType, UserRole userRole) {
        return commissionDao.getCommission(operationType, userRole);
    }

    @Transactional
    public BigDecimal getCommissionMerchant(String merchant, String currency, OperationType operationType) {
        if (!(operationType == INPUT || operationType == OUTPUT)) {
            throw new IllegalArgumentException("Invalid operation type");
        }
        return commissionDao.getCommissionMerchant(merchant, currency, operationType);
    }

    @Transactional
    public BigDecimal getCommissionMerchant(Integer merchantId, Integer currencyId, OperationType operationType) {
        if (!(operationType == INPUT || operationType == OUTPUT || operationType == OperationType.USER_TRANSFER)) {
            throw new IllegalArgumentException("Invalid operation type: " + operationType);
        }
        return commissionDao.getCommissionMerchant(merchantId, currencyId, operationType);
    }

    public BigDecimal getMinFixedCommission(Integer currencyId, Integer merchantId) {
        return commissionDao.getMinFixedCommission(currencyId, merchantId);
    }

    @Transactional
    public Map<String, String> computeCommissionAndMapAllToString(
            Integer userId,
            BigDecimal amount,
            OperationType type,
            Integer currencyId,
            Integer merchantId,
            Locale locale,
            String destinationTag) {
        Map<String, String> result = new HashMap<>();
        CommissionDataDto commissionData = normalizeAmountAndCalculateCommission(userId, amount, type, currencyId, merchantId, destinationTag);
        result.put("amount", commissionData.getAmount().toPlainString());
        if (!commissionData.getSpecificMerchantComissionCount()) {

            String merchantCommissionRate = messageSource.getMessage("merchant.commission.rateWithLimit",
                    new Object[]{BigDecimalProcessing.formatLocale(commissionData.getMerchantCommissionRate(), locale, false)
                            + commissionData.getMerchantCommissionUnit(),
                            String.join("", BigDecimalProcessing.formatLocale(commissionData.getMinMerchantCommissionAmount(), locale, false),
                                    " ", currencyService.getCurrencyName(currencyId))}, locale);

            result.put("merchantCommissionRate", String.join("", "(", merchantCommissionRate, ")"));

        } else {
            result.put("merchantCommissionRate", "");
        }
        result.put("merchantCommissionAmount", commissionData.getMerchantCommissionAmount().toPlainString());
        result.put("companyCommissionRate", String.join("", "(", BigDecimalProcessing.formatLocale(commissionData.getCompanyCommissionRate(), locale, false),
                commissionData.getCompanyCommissionUnit(), ")"));
        result.put("companyCommissionAmount", commissionData.getCompanyCommissionAmount().toPlainString());
        result.put("totalCommissionAmount", commissionData.getTotalCommissionAmount().toPlainString());
        result.put("resultAmount", commissionData.getResultAmount().toPlainString());
        return result;
    }

    @Transactional
    public CommissionDataDto normalizeAmountAndCalculateCommission(
            Integer userId,
            BigDecimal amount,
            OperationType type,
            Integer currencyId,
            Integer merchantId, String destinationTag) {
        Boolean specMerchantComissionCount = false;
        Commission companyCommission;
        if (type == OUTPUT && currencyService.isIco(currencyId)) {
            companyCommission = Commission.zeroComission();
        } else {
            companyCommission = findCommissionByTypeAndRole(type, userService.getUserRoleFromDB(userId));
        }
        BigDecimal companyCommissionRate = companyCommission.getValue();
        String companyCommissionUnit = "%";
        Merchant merchant = merchantService.findById(merchantId);
        if (!(merchant.getProcessType() == MerchantProcessType.CRYPTO) || amount.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal merchantCommissionRate = getCommissionMerchant(merchantId, currencyId, type);
            BigDecimal merchantMinFixedCommission = getMinFixedCommission(currencyId, merchantId);
            BigDecimal merchantCommissionAmount;
            BigDecimal companyCommissionAmount;
            String merchantCommissionUnit = "%";
            if (type == INPUT) {
                int currencyScale = merchantService.getMerchantCurrencyScaleByMerchantIdAndCurrencyId(merchantId, currencyId).getScaleForRefill();
                amount = amount.setScale(currencyScale, ROUND_DOWN);
                merchantCommissionAmount = BigDecimalProcessing.doAction(amount, merchantCommissionRate, MULTIPLY_PERCENT);
                companyCommissionAmount = BigDecimalProcessing.doAction(amount.subtract(merchantCommissionAmount), companyCommissionRate, MULTIPLY_PERCENT);
            } else if (type == OUTPUT) {
                IWithdrawable wMerchant = (IWithdrawable) merchantServiceContext.getMerchantService(merchantId);
                int currencyScale = merchantService.getMerchantCurrencyScaleByMerchantIdAndCurrencyId(merchantId, currencyId).getScaleForWithdraw();
                amount = amount.setScale(currencyScale, ROUND_DOWN);
                companyCommissionAmount = BigDecimalProcessing.doAction(amount, companyCommissionRate, MULTIPLY_PERCENT).setScale(currencyScale, ROUND_UP);
                if (wMerchant.specificWithdrawMerchantCommissionCountNeeded()) {
                    merchantCommissionAmount = wMerchant.countSpecCommission(amount, destinationTag, merchantId);
                    specMerchantComissionCount = true;
                } else {
                    merchantCommissionAmount = BigDecimalProcessing.doAction(amount.subtract(companyCommissionAmount), merchantCommissionRate, MULTIPLY_PERCENT).setScale(currencyScale, ROUND_UP);
                }
                if (merchantCommissionAmount.compareTo(merchantMinFixedCommission) < 0) {
                    merchantCommissionAmount = merchantMinFixedCommission;
                }
            } else if (type == USER_TRANSFER) {
                int currencyScale = merchantService.getMerchantCurrencyScaleByMerchantIdAndCurrencyId(merchantId, currencyId).getScaleForTransfer();
                amount = amount.setScale(currencyScale, ROUND_DOWN);
                companyCommissionAmount = BigDecimal.ZERO;
                merchantCommissionRate = BigDecimalProcessing.doAction(merchantCommissionRate, companyCommissionRate, ADD);
                merchantCommissionAmount = BigDecimalProcessing.doAction(amount, merchantCommissionRate, MULTIPLY_PERCENT).setScale(currencyScale, ROUND_HALF_UP);
                if (merchantCommissionAmount.compareTo(merchantMinFixedCommission) < 0) {
                    merchantCommissionAmount = merchantMinFixedCommission;
                }
            } else {
                throw new IllegalOperationTypeException(type.name());
            }
            BigDecimal totalCommissionAmount = BigDecimalProcessing.doAction(merchantCommissionAmount, companyCommissionAmount, ADD);
            BigDecimal totalAmount = BigDecimalProcessing.doAction(amount, totalCommissionAmount, SUBTRACT);
            if (totalAmount.compareTo(ZERO) <= 0) {
                throw new InvalidAmountException(String.format("Commission %s exceeds amount %s",
                        BigDecimalProcessing.formatNonePoint(totalCommissionAmount, false),
                        BigDecimalProcessing.formatNonePoint(amount, false)));
            }
            return new CommissionDataDto(
                    amount,
                    merchantCommissionRate,
                    merchantMinFixedCommission,
                    merchantCommissionUnit,
                    merchantCommissionAmount,
                    companyCommission,
                    companyCommissionRate,
                    companyCommissionUnit,
                    companyCommissionAmount,
                    totalCommissionAmount,
                    totalAmount,
                    specMerchantComissionCount

            );
        } else {
            return new CommissionDataDto(
                    ZERO,
                    ZERO,
                    ZERO,
                    "",
                    ZERO,
                    companyCommission,
                    companyCommissionRate,
                    companyCommissionUnit,
                    ZERO,
                    ZERO,
                    ZERO,
                    specMerchantComissionCount
            );
        }
    }


    @Transactional
    public BigDecimal calculateCommissionForRefillAmount(BigDecimal amount, Integer commissionId) {
        BigDecimal companyCommissionRate = commissionDao.getCommissionById(commissionId).getValue();
        return BigDecimalProcessing.doAction(amount, companyCommissionRate, MULTIPLY_PERCENT);
    }

    @Override
    public Commission getDefaultCommission(OperationType operationType) {
        return commissionDao.getDefaultCommission(operationType);
    }

}
