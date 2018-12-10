package com.exrates.inout.service.merchant;

import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.util.Map;

public interface YandexKassaService extends IRefillable, IWithdrawable {

    Map<String, String> preparePayment(CreditsOperation creditsOperation, String email);

    boolean confirmPayment(final Map<String, String> params);

    @Override
    default Boolean createdRefillRequestRecordNeeded() {
        return null;
    }

    @Override
    default Boolean needToCreateRefillRequestRecord() {
        return null;
    }

    @Override
    default Boolean toMainAccountTransferringConfirmNeeded() {
        return null;
    }

    @Override
    default Boolean generatingAdditionalRefillAddressAvailable() {
        return null;
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return null;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }
}
