package com.exrates.inout.service.bitshares;

import me.exrates.model.Currency;
import me.exrates.model.Merchant;
import me.exrates.model.dto.RefillRequestAcceptDto;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.math.BigDecimal;

public interface BitsharesService extends IRefillable , IWithdrawable {


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
        return true;
    }

    @Override
    default String additionalRefillFieldName() {
        return "MEMO";
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return true;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default boolean isValidDestinationAddress(String address) {
        return false;
    }

    Merchant getMerchant();

    Currency getCurrency();

    RefillRequestAcceptDto createRequest(String hash, String address, BigDecimal amount);

    void putOnBchExam(RefillRequestAcceptDto requestAcceptDto);
}
