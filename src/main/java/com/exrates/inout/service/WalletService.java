package com.exrates.inout.service;


import com.exrates.inout.domain.dto.TransferDto;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.domain.other.WalletOperationData;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;

public interface WalletService {

    int getWalletId(int userId, int currencyId);

    BigDecimal getWalletABalance(int walletId);

    boolean ifEnoughMoney(int walletId, BigDecimal amountForCheck);

    Wallet findByUserAndCurrency(User user, Currency currency);

    Wallet create(User user, Currency currency);

    WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description);

    WalletTransferStatus walletBalanceChange(WalletOperationData walletOperationData);

    @Transactional(rollbackFor = Exception.class)
    TransferDto transferCostsToUser(Integer fromUserWalletId, Integer userId, BigDecimal amount, BigDecimal comission,
                                    Locale locale, int sourceId);

    @Transactional(rollbackFor = Exception.class)
    String transferCostsToUser(Integer userId, Integer fromUserWalletId, Integer toUserId, BigDecimal amount,
                               BigDecimal comission, Locale locale, int sourceId);

    int getWalletIdAndBlock(Integer userId, Integer currencyId);

}