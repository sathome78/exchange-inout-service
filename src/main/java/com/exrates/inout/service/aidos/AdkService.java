package com.exrates.inout.service.aidos;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.service.BitcoinLikeCurrency;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import com.exrates.inout.service.MerchantService;

import java.math.BigDecimal;



public interface AdkService extends BitcoinLikeCurrency, IRefillable, IWithdrawable {


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

    Merchant getMerchant();

    Currency getCurrency();

    MerchantService getMerchantService();

    RefillRequestAcceptDto createRequest(String address, String hash, BigDecimal amount);

    void putOnBchExam(RefillRequestAcceptDto requestAcceptDto);

    String getBalance();
}
