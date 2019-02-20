package com.exrates.inout.service.apollo;

import me.exrates.model.Currency;
import me.exrates.model.Merchant;
import me.exrates.model.dto.RefillRequestAcceptDto;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.math.BigDecimal;

public interface ApolloService extends IRefillable, IWithdrawable {

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
    };

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default String additionalRefillFieldName() {
        return "MESSAGE";
    }

    @Override
    default String additionalWithdrawFieldName() {
        return "MESSAGE";
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return false;
    }

    RefillRequestAcceptDto createRequest(String address, BigDecimal amount, String hash);

    void putOnBchExam(RefillRequestAcceptDto requestAcceptDto);

    Merchant getMerchant();

    Currency getCurrency();
}
