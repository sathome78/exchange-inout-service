package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.domain.other.WalletOperationData;

import java.math.BigDecimal;

public interface WalletDao {

    BigDecimal getWalletABalance(int walletId);

    int getWalletId(int userId, int currencyId);

    Wallet findByUserAndCurrency(int userId, int currencyId);

    Wallet findById(Integer walletId);

    Wallet createWallet(User user, int currencyId);

    boolean update(Wallet wallet);

    WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description);

    WalletTransferStatus walletBalanceChange(WalletOperationData walletOperationData);

    int getWalletIdAndBlock(Integer userId, Integer currencyId);
}