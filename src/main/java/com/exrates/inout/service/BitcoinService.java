package com.exrates.inout.service;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.event.BtcBlockEvent;
import com.exrates.inout.event.BtcWalletEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

public interface BitcoinService extends IRefillable, IWithdrawable {

    @EventListener(value = BtcWalletEvent.class)
    void onPayment(BtcTransactionDto transactionDto);

    @EventListener(value = BtcBlockEvent.class)
    void onIncomingBlock(BtcBlockDto blockDto);

    @Scheduled(initialDelay = 5 * 60000, fixedDelay = 12 * 60 * 60000)
    void backupWallet();

    BtcWalletInfoDto getWalletInfo();

    default Boolean createdRefillRequestRecordNeeded() {
        return false;
    }

    default Boolean needToCreateRefillRequestRecord() {
        return false;
    }

    default Boolean toMainAccountTransferringConfirmNeeded() {
        return false;
    }

    default Boolean generatingAdditionalRefillAddressAvailable() {
        return true;
    }

    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

}
