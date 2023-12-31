package com.exrates.inout.service.monero;


import com.exrates.inout.service.IMerchantService;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

/**
 * Created by ajet on
 */
public interface MoneroService extends IMerchantService, IRefillable, IWithdrawable {

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

    default void sendToMainAccount(){};
}
