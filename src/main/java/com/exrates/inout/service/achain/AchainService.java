package com.exrates.inout.service.achain;

import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

public interface AchainService extends IWithdrawable, IRefillable {

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
        return false;
    };

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return true;
    }

    @Override
    default boolean concatAdditionalToMainAddress() {
        return true;
    }

}
