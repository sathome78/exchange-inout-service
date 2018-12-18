package com.exrates.inout.dao;

import com.exrates.inout.domain.ExternalWalletsDto;
import com.exrates.inout.domain.dto.MyWalletsDetailedDto;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.domain.other.WalletOperationData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public interface WalletDao {

    BigDecimal getWalletABalance(int walletId);

    int getWalletId(int userId, int currencyId);

    int createNewWallet(Wallet wallet);

    List<MyWalletsDetailedDto> getAllWalletsForUserDetailed(String email, List<Integer> currencyIds, List<Integer> withdrawStatusIds, Locale locale);

    Wallet findByUserAndCurrency(int userId, int currencyId);

    Wallet findById(Integer walletId);

    Wallet createWallet(User user, int currencyId);

    boolean update(Wallet wallet);

    WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description);

    WalletTransferStatus walletBalanceChange(WalletOperationData walletOperationData);

    void addToWalletBalance(Integer walletId, BigDecimal addedAmountActive, BigDecimal addedAmountReserved);

    int getWalletIdAndBlock(Integer userId, Integer currencyId);

    List<ExternalWalletsDto> getExternalWallets();
}