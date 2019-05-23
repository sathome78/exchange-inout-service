package com.exrates.inout.service.aisi;

import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import com.exrates.inout.service.aisi.AisiCurrencyServiceImpl.Transaction;

import java.util.List;

public interface AisiService extends IRefillable, IWithdrawable {

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
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    void checkAddressForTransactionReceive(List<String> listOfAddress, Transaction transaction);

    void onTransactionReceive(Transaction transaction);
}
