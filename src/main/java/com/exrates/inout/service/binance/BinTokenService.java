package com.exrates.inout.service.binance;


import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

public interface BinTokenService extends IRefillable, IWithdrawable {

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
        return true;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return true;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return true;
    }

    @Override
    default String additionalWithdrawFieldName() {
        return "MEMO";
    }

    @Override
    default String additionalRefillFieldName() {
        return "MEMO";
    }

    String getMerchantName();

    String getCurrencyName();
}
