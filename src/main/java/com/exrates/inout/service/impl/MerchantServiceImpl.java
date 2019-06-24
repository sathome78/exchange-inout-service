package com.exrates.inout.service.impl;

import com.exrates.inout.dao.MerchantDao;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.enums.MerchantProcessType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.service.*;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.exrates.inout.domain.enums.OperationType.OUTPUT;
import static com.exrates.inout.domain.enums.OperationType.USER_TRANSFER;
import static java.math.BigDecimal.ROUND_DOWN;
import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class MerchantServiceImpl implements MerchantService {

    private static final Logger LOG = LogManager.getLogger("merchant");

    private String walletPropsFolder = "/opt/properties/"; //todo extract

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MerchantServiceContext merchantServiceContext;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private CurrencyService currencyService;

    private static final BigDecimal HUNDREDTH = new BigDecimal(100L);

    @Override
    public List<Merchant> findAllByCurrency(Currency currency) {
        return merchantDao.findAllByCurrency(currency.getId());
    }


    @Override
    public List<Merchant> findAll() {
        return merchantDao.findAll();
    }

    @Override
    public Merchant findById(int id) {
        return merchantDao.findById(id);
    }

    @Override
    public Merchant findByName(String name) {
        return merchantDao.findByName(name);
    }

    @Override
    public List<MerchantCurrency> getAllUnblockedForOperationTypeByCurrencies(List<Integer> currenciesId, OperationType operationType) {
        if (currenciesId.isEmpty()) {
            return null;
        }
        return merchantDao.findAllUnblockedForOperationTypeByCurrencies(currenciesId, operationType);
    }

    @Override
    public List<MerchantCurrencyApiDto> findNonTransferMerchantCurrencies(Integer currencyId) {
        return findMerchantCurrenciesByCurrencyAndProcessTypes(currencyId, Arrays.stream(MerchantProcessType.values())
                .filter(item -> item != MerchantProcessType.TRANSFER).map(Enum::name).collect(Collectors.toList()));
    }

    @Override
    public Optional<MerchantCurrency> findByMerchantAndCurrency(int merchantId, int currencyId) {
        return merchantDao.findByMerchantAndCurrency(merchantId, currencyId);
    }

    @Override
    public List<TransferMerchantApiDto> findTransferMerchants() {
        List<TransferMerchantApiDto> result = merchantDao.findTransferMerchants();
        result.forEach(item -> {
            IMerchantService merchantService = merchantServiceContext.getMerchantService(item.getServiceBeanName());
            if (merchantService instanceof ITransferable) {
                ITransferable transferService = (ITransferable) merchantService;
                item.setIsVoucher(transferService.isVoucher());
                item.setRecipientUserIsNeeded(transferService.recipientUserIsNeeded());
            }
        });
        return result;
    }


    private List<MerchantCurrencyApiDto> findMerchantCurrenciesByCurrencyAndProcessTypes(Integer currencyId, List<String> processTypes) {
        List<MerchantCurrencyApiDto> result = merchantDao.findAllMerchantCurrencies(currencyId, userService.getUserRoleFromSecurityContext(), processTypes);
        result.forEach(item -> {
            try {
                IMerchantService merchantService = merchantServiceContext.getMerchantService(item.getServiceBeanName());
                if (merchantService instanceof IWithdrawable) {
                    IWithdrawable withdrawService = (IWithdrawable) merchantService;
                    if (withdrawService.additionalTagForWithdrawAddressIsUsed()) {
                        item.setAdditionalFieldName(withdrawService.additionalWithdrawFieldName());
                        item.setWithdrawCommissionDependsOnDestinationTag(withdrawService.comissionDependsOnDestinationTag());
                    }
                } else if (merchantService instanceof IRefillable) {
                    if (((IRefillable) merchantService).additionalFieldForRefillIsUsed()) {
                        item.setAdditionalFieldName(((IRefillable) merchantService).additionalRefillFieldName());
                    }
                }
            } catch (MerchantServiceNotFoundException | MerchantServiceBeanNameNotDefinedException e) {
                LOG.warn(e);
            }
        });
        return result;
    }

    @Override
    @Transactional
    public BigDecimal getMinSum(Integer merchantId, Integer currencyId) {
        return merchantDao.getMinSum(merchantId, currencyId);
    }

    @Override
    @Transactional
    public void checkAmountForMinSum(Integer merchantId, Integer currencyId, BigDecimal amount) {
        if (amount.compareTo(getMinSum(merchantId, currencyId)) < 0) {
            throw new InvalidAmountException(String.format("merchant: %s currency: %s amount %s", merchantId, currencyId, amount.toString()));
        }
    }

    /*============================*/


    @Override
    @Transactional
    public void setMinSum(double merchantId, double currencyId, double minSum) {
        merchantDao.setMinSum(merchantId, currencyId, minSum);
    }

    @Override
    @Transactional
    public MerchantCurrencyLifetimeDto getMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(
            Integer merchantId,
            Integer currencyId) {
        return merchantDao.findMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(merchantId, currencyId);
    }

    @Override
    @Transactional
    public MerchantCurrencyScaleDto getMerchantCurrencyScaleByMerchantIdAndCurrencyId(
            Integer merchantId,
            Integer currencyId) {
        MerchantCurrencyScaleDto result = merchantDao.findMerchantCurrencyScaleByMerchantIdAndCurrencyId(merchantId, currencyId);
        Optional.ofNullable(result.getScaleForRefill()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        Optional.ofNullable(result.getScaleForWithdraw()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        Optional.ofNullable(result.getScaleForTransfer()).orElseThrow(() -> new ScaleForAmountNotSetException("currency: " + currencyId));
        return result;
    }

    @Override
    @Transactional
    public void checkMerchantIsBlocked(Integer merchantId, Integer currencyId, OperationType operationType) {
        boolean isBlocked = merchantDao.checkMerchantBlock(merchantId, currencyId, operationType);
        if (isBlocked) {
            throw new MerchantCurrencyBlockedException("Operation " + operationType + " is blocked for this currency! ");
        }
    }

    @Override
    public Optional<String> getCoreWalletPassword(String merchantName, String currencyName) {
        Properties props = getPassMerchantProperties(merchantName);
        return Optional.ofNullable(props.getProperty("wallet.password"));
    }

    /*pass file format : classpath: merchants/pass/<merchant>_pass.properties
     * stored values: wallet.password
     *                node.bitcoind.rpc.user
     *                node.bitcoind.rpc.password
     * */
    @SneakyThrows
    @Override
    public Properties getPassMerchantProperties(String merchantName) {
        Properties props = new Properties();
        String fullPath = String.join("", walletPropsFolder, merchantName, "_pass.properties");
        FileInputStream inputStream = new FileInputStream(new File(fullPath));
        props.load(inputStream);
        return props;
    }

    @Override
    public Map<String, String> computeCommissionAndMapAllToString(final BigDecimal amount,
                                                                  final OperationType type,
                                                                  final String currency,
                                                                  final String merchant) {
        final Map<String, String> result = new HashMap<>();
        final BigDecimal commission = commissionService.findCommissionByTypeAndRole(type, userService.getUserRoleFromSecurityContext()).getValue();
        final BigDecimal commissionMerchant = type == USER_TRANSFER ? BigDecimal.ZERO : commissionService.getCommissionMerchant(merchant, currency, type);
        final BigDecimal commissionTotal = commission.add(commissionMerchant).setScale(currencyService.resolvePrecisionByOperationType(currency, type), ROUND_HALF_UP);
        BigDecimal commissionAmount = amount.multiply(commissionTotal).divide(HUNDREDTH).setScale(currencyService.resolvePrecisionByOperationType(currency, type), ROUND_HALF_UP);
        String commissionString = Stream.of("(", commissionTotal.stripTrailingZeros().toString(), "%)").collect(Collectors.joining(""));
        if (type == OUTPUT) {
            BigDecimal merchantMinFixedCommission = commissionService.getMinFixedCommission(currencyService.findByName(currency).getId(), this.findByName(merchant).getId());
            if (commissionAmount.compareTo(merchantMinFixedCommission) < 0) {
                commissionAmount = merchantMinFixedCommission;
                commissionString = "";
            }
        }
        LOG.debug("commission: " + commissionString);
        final BigDecimal resultAmount = type != OUTPUT ? amount.add(commissionAmount).setScale(currencyService.resolvePrecisionByOperationType(currency, type), ROUND_HALF_UP) :
                amount.subtract(commissionAmount).setScale(currencyService.resolvePrecisionByOperationType(currency, type), ROUND_DOWN);
        if (resultAmount.signum() <= 0) {
            throw new InvalidAmountException("merchants.invalidSum");
        }
        result.put("commission", commissionString);
        result.put("commissionAmount", currencyService.amountToString(commissionAmount, currency));
        result.put("amount", currencyService.amountToString(resultAmount, currency));
        return result;
    }

    @Override
    public void checkDestinationTag(Integer merchantId, String destinationTag) throws CheckDestinationTagException {
        IMerchantService merchantService = merchantServiceContext.getMerchantService(merchantId);
        if (merchantService instanceof IWithdrawable && ((IWithdrawable) merchantService).additionalTagForWithdrawAddressIsUsed()) {
            ((IWithdrawable) merchantService).checkDestinationTag(destinationTag);
        }
    }

    @Override
    public boolean isValidDestinationAddress(Integer merchantId, String address) {

        IMerchantService merchantService = merchantServiceContext.getMerchantService(merchantId);
        if (merchantService instanceof IWithdrawable) {
            return ((IWithdrawable) merchantService).isValidDestinationAddress(address);
        } else {
            return true;
        }
    }

    @Override
    public List<String> getWarningsForMerchant(OperationType operationType, Integer merchantId, Locale locale) {
        UserCommentTopicEnum commentTopic;
        switch (operationType) {
            case INPUT:
                commentTopic = UserCommentTopicEnum.REFILL_MERCHANT_WARNING;
                break;
            case OUTPUT:
                commentTopic = UserCommentTopicEnum.WITHDRAW_MERCHANT_WARNING;
                break;
            default:
                throw new IllegalArgumentException(String.format("Illegal operation type %s", operationType.name()));
        }
        List<String> result = currencyService.getWarningForMerchant(merchantId, commentTopic);
        LOG.info("Warning result: " + result);
        List<String> resultLocalized = currencyService.getWarningForMerchant(merchantId, commentTopic).stream()
                .map(code -> messageSource.getMessage(code, null, locale)).collect(Collectors.toList());
        LOG.info("Localized result: " + resultLocalized);
        return resultLocalized;
    }

    @Override
    public List<Integer> getIdsByProcessType(List<String> processType) {
        return merchantDao.findCurrenciesIdsByType(processType);
    }

    @Override
    public boolean getSubtractFeeFromAmount(Integer merchantId, Integer currencyId) {
        return merchantDao.getSubtractFeeFromAmount(merchantId, currencyId);
    }

    @Override
    public void setSubtractFeeFromAmount(Integer merchantId, Integer currencyId, boolean subtractFeeFromAmount) {
        merchantDao.setSubtractFeeFromAmount(merchantId, currencyId, subtractFeeFromAmount);
    }

    @Override
    public List<MerchantCurrencyBasicInfoDto> findTokenMerchantsByParentId(Integer parentId) {
        return merchantDao.findTokenMerchantsByParentId(parentId);
    }

    @Override
    @Transactional
    public List<MerchantCurrencyLifetimeDto> getMerchantCurrencyWithRefillLifetime() {
        return merchantDao.findMerchantCurrencyWithRefillLifetime();
    }

}
