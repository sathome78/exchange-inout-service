package com.exrates.inout.service.lisk;

import me.exrates.model.dto.merchants.lisk.LiskAccount;
import me.exrates.model.dto.merchants.lisk.LiskTransaction;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.math.BigDecimal;
import java.util.List;

public interface LiskService extends IRefillable, IWithdrawable {

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
        return false;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return null;
    }

    void processTransactionsForKnownAddresses();

    LiskTransaction getTransactionById(String txId);

    List<LiskTransaction> getTransactionsByRecipient(String recipientAddress);

    String sendTransaction(String secret, Long amount, String recipientId);

    String sendTransaction(String secret, BigDecimal amount, String recipientId);

    LiskAccount createNewLiskAccount(String secret);

    LiskAccount getAccountByAddress(String address);
}
