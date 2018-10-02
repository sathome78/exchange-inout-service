package com.exrates.inout.service.impl;

import com.exrates.inout.dao.WalletDao;
import com.exrates.inout.domain.dto.TransferDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.other.WalletOperationData;
import com.exrates.inout.exceptions.BalanceChangeException;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.exceptions.UserNotFoundException;
import com.exrates.inout.exceptions.WalletNotFoundException;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

@Log4j2
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

    private static final int decimalPlaces = 9;

    private final WalletDao walletDao;
    private final CurrencyService currencyService;
    private final UserService userService;
    private final CommissionService commissionService;
    private final CompanyWalletService companyWalletService;
    private final NotificationService notificationService;
    private final MessageSource messageSource;

    @Autowired
    public WalletServiceImpl(WalletDao walletDao, CurrencyService currencyService, UserService userService, CommissionService commissionService, CompanyWalletService companyWalletService, NotificationService notificationService, MessageSource messageSource) {
        this.walletDao = walletDao;
        this.currencyService = currencyService;
        this.userService = userService;
        this.commissionService = commissionService;
        this.companyWalletService = companyWalletService;
        this.notificationService = notificationService;
        this.messageSource = messageSource;
    }

    public int getWalletId(int userId, int currencyId) {
        return walletDao.getWalletId(userId, currencyId);
    }

    @Transactional(propagation = Propagation.NESTED)
    public BigDecimal getWalletABalance(int walletId) {
        return walletDao.getWalletABalance(walletId);
    }


    @Transactional(readOnly = true)
    public boolean ifEnoughMoney(int walletId, BigDecimal amountForCheck) {
        BigDecimal balance = getWalletABalance(walletId);
        boolean result = balance.compareTo(amountForCheck) >= 0;
        if (!result) {
            log.error(String.format("Not enough wallet money: wallet id %s, actual amount %s but needed %s", walletId,
                    BigDecimalProcessing.formatNonePoint(balance, false),
                    BigDecimalProcessing.formatNonePoint(amountForCheck, false)));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Wallet findByUserAndCurrency(User user, Currency currency) {
        return walletDao.findByUserAndCurrency(user.getId(), currency.getId());
    }

    public Wallet create(User user, Currency currency) {
        final Wallet wallet = walletDao.createWallet(user, currency.getId());
        wallet.setName(currency.getName());
        return wallet;
    }

    @Transactional
    public WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description) {
        return walletDao.walletInnerTransfer(walletId, amount, sourceType, sourceId, description);
    }

    @Override
    public WalletTransferStatus walletBalanceChange(final WalletOperationData walletOperationData) {
        return walletDao.walletBalanceChange(walletOperationData);
    }

    private void changeWalletActiveBalance(BigDecimal amount, Wallet wallet, OperationType operationType,
                                           TransactionSourceType transactionSourceType,
                                           BigDecimal specialCommissionAmount, Integer sourceId) {
        WalletOperationData walletOperationData = new WalletOperationData();
        walletOperationData.setWalletId(wallet.getId());
        walletOperationData.setAmount(amount);
        walletOperationData.setBalanceType(WalletOperationData.BalanceType.ACTIVE);
        walletOperationData.setOperationType(operationType);
        walletOperationData.setSourceId(sourceId);
        Commission commission = commissionService.findCommissionByTypeAndRole(operationType, userService.getUserRoleFromSecurityContext());
        walletOperationData.setCommission(commission);
        BigDecimal commissionAmount = specialCommissionAmount == null ?
                BigDecimalProcessing.doAction(amount, commission.getValue(), ActionType.MULTIPLY_PERCENT) : specialCommissionAmount;
        walletOperationData.setCommissionAmount(commissionAmount);
        walletOperationData.setSourceType(transactionSourceType);
        WalletTransferStatus status = walletBalanceChange(walletOperationData);
        if (status != WalletTransferStatus.SUCCESS) {
            throw new BalanceChangeException(status.name());
        }
        if (commissionAmount.signum() > 0) {

            CompanyWallet companyWallet = companyWalletService.findByCurrency(currencyService.getById(wallet.getCurrencyId()));
            companyWalletService.deposit(companyWallet, BigDecimal.ZERO, commissionAmount);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public TransferDto transferCostsToUser(Integer fromUserWalletId, Integer toUserId, BigDecimal amount,
                                           BigDecimal commissionAmount, Locale locale, int sourceId) {
        if (amount.signum() <= 0) {
            throw new InvalidAmountException(messageSource.getMessage("transfer.negativeAmount", null, locale));
        }
        Wallet fromUserWallet = walletDao.findById(fromUserWalletId);
        Integer currencyId = fromUserWallet.getCurrencyId();
        BigDecimal inputAmount = BigDecimalProcessing.doAction(amount, commissionAmount, ActionType.SUBTRACT);
        log.debug(commissionAmount.toString());
        log.debug(inputAmount.toString());
        if (inputAmount.compareTo(fromUserWallet.getActiveBalance()) > 0) {
            throw new InvalidAmountException(messageSource.getMessage("transfer.invalidAmount", null, locale));
        }
        Wallet toUserWallet = walletDao.findByUserAndCurrency(toUserId, currencyId);
        if (toUserWallet == null) {
            throw new WalletNotFoundException(messageSource.getMessage("transfer.walletNotFound", null, locale));
        }
        changeWalletActiveBalance(amount, fromUserWallet, OperationType.OUTPUT,
                TransactionSourceType.USER_TRANSFER, commissionAmount, sourceId);
        changeWalletActiveBalance(inputAmount, toUserWallet, OperationType.INPUT,
                TransactionSourceType.USER_TRANSFER, BigDecimal.ZERO, sourceId);
        String notyAmount = inputAmount.setScale(decimalPlaces, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return TransferDto.builder()
                .comissionAmount(commissionAmount)
                .notyAmount(notyAmount)
                .walletUserFrom(fromUserWallet)
                .walletUserTo(toUserWallet)
                .initialAmount(amount)
                .currencyId(currencyId)
                .userFromId(fromUserWallet.getUser().getId())
                .userToId(toUserId)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public String transferCostsToUser(Integer userId, Integer fromUserWalletId, Integer toUserId, BigDecimal amount,
                                      BigDecimal comission, Locale locale, int sourceId) {
        User toUser = userService.getUserById(toUserId);
        String toUserNickname = toUser.getNickname() != null ? toUser.getNickname() : toUser.getEmail();
        if (toUserId == 0) {
            throw new UserNotFoundException(messageSource.getMessage("transfer.userNotFound", new Object[]{toUserNickname}, locale));
        }
        TransferDto dto = transferCostsToUser(fromUserWalletId, toUserId, amount, comission, locale, sourceId);
        String currencyName = currencyService.getCurrencyName(dto.getCurrencyId());
        String result = messageSource.getMessage("transfer.successful", new Object[]{dto.getNotyAmount(), currencyName, toUserNickname}, locale);
        sendNotificationsAboutTransfer(userId, dto.getNotyAmount(), currencyName, dto.getUserToId(), toUserNickname);
        return result;
    }


    private void sendNotificationsAboutTransfer(int fromUserId, String notyAmount, String currencyName, int toUserId, String toNickName) {
        log.debug("from {} to {}", fromUserId, toUserId);
        notificationService.notifyUser(fromUserId, NotificationEvent.IN_OUT, "wallets.transferTitle",
                "transfer.successful", new Object[]{notyAmount, currencyName, toNickName});
        notificationService.notifyUser(toUserId, NotificationEvent.IN_OUT, "wallets.transferTitle",
                "transfer.received", new Object[]{notyAmount, currencyName});
    }

    public int getWalletIdAndBlock(Integer userId, Integer currencyId) {
        return walletDao.getWalletIdAndBlock(userId, currencyId);
    }

}
