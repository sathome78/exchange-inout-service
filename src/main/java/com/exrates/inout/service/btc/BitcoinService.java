package com.exrates.inout.service.btc;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.event.BtcBlockEvent;
import com.exrates.inout.event.BtcWalletEvent;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public interface BitcoinService extends IRefillable, IWithdrawable {

    boolean isRawTxEnabled();

    @EventListener(value = BtcWalletEvent.class)
    void onPayment(BtcTransactionDto transactionDto);

    @EventListener(value = BtcBlockEvent.class)
    void onIncomingBlock(BtcBlockDto blockDto);

    @Scheduled(initialDelay = 5 * 60000, fixedDelay = 12 * 60 * 60000)
    void backupWallet();

    BtcWalletInfoDto getWalletInfo();

    List<BtcTransactionHistoryDto> listAllTransactions();

    BigDecimal estimateFee();

    String getEstimatedFeeString();

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

    void setTxFee(BigDecimal fee);

    void submitWalletPassword(String password);

    List<BtcPaymentResultDetailedDto> sendToMany(List<BtcWalletPaymentItemDto> payments);

    BtcAdminPreparedTxDto prepareRawTransactions(List<BtcWalletPaymentItemDto> payments);

    BtcAdminPreparedTxDto updateRawTransactions(List<BtcPreparedTransactionDto> preparedTransactions);

    List<BtcPaymentResultDetailedDto> sendRawTransactions(List<BtcPreparedTransactionDto> preparedTransactions);

    void scanForUnprocessedTransactions(@Nullable String blockHash);

    String getNewAddressForAdmin();

    void setSubtractFeeFromAmount(boolean subtractFeeFromAmount);

    boolean getSubtractFeeFromAmount();
}
