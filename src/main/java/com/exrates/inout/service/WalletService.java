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

    Wallet findByUserAndCurrency(int userId, int currencyId);

    Wallet create(User user, Currency currency);

    void depositActiveBalance(Wallet wallet, BigDecimal sum);

    void withdrawReservedBalance(Wallet wallet, BigDecimal sum);

    /**
     * Transfers money between active balance the wallet and reserved balance the wallet
     * and creates corresponding transaction
     *
     * @param walletId   is wallet ID
     * @param amount     amount to transfer
     * @param sourceType type the operation that caused the transfer
     * @param sourceId   ID the operation in the table that corresponds to sourceType
     * @return WalletTransferStatus with detail about result
     * @author ValkSam
     */

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