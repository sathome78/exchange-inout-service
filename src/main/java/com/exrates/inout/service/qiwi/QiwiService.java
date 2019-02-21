package com.exrates.inout.service.qiwi;

//exrates.model.dto.qiwi.response.QiwiResponseTransaction;

import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseTransaction;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

public interface QiwiService extends IRefillable, IWithdrawable {

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
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return true;
    };

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default String additionalRefillFieldName() {
        return "Memo";
    }

    @Override
    default boolean comissionDependsOnDestinationTag() {
        return false;
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return false;
    }

    void onTransactionReceive(QiwiResponseTransaction transaction, String amount, String currencyName, String merchant);
}
