package com.exrates.inout.service.zil;

import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.util.Map;

public interface ZilService extends IRefillable, IWithdrawable {

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

    void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException;
}
