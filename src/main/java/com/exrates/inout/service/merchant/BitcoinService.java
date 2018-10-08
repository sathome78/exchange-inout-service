package com.exrates.inout.service.merchant;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.event.BtcBlockEvent;
import com.exrates.inout.event.BtcWalletEvent;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;

public interface BitcoinService extends IRefillable, IWithdrawable {

    @EventListener(value = BtcWalletEvent.class)
    void onPayment(BtcTransactionDto transactionDto);

    @EventListener(value = BtcBlockEvent.class)
    void onIncomingBlock(BtcBlockDto blockDto);

    @Scheduled(initialDelay = 5 * 60000, fixedDelay = 12 * 60 * 60000)
    void backupWallet();

    BtcWalletInfoDto getWalletInfo();

    BigDecimal estimateFee();

    BigDecimal getActualFee();

    @Override
    default Boolean createdRefillRequestRecordNeeded() {
        return false;
    }

    @Override
    default Boolean needToCreateRefillRequestRecord() {
        return false;
    }

    @Override
    default Boolean toMainAccountTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default Boolean generatingAdditionalRefillAddressAvailable() {
        return true;
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

}
