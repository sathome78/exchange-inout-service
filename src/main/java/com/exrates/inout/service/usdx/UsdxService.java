package com.exrates.inout.service.usdx;

import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import com.exrates.inout.service.usdx.model.UsdxTransaction;
import com.exrates.inout.service.usdx.model.UsdxTxSendAdmin;

import java.util.Map;

public interface UsdxService extends IRefillable, IWithdrawable {

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
        return "MEMO-TEXT";
    }

    @Override
    default String additionalWithdrawFieldName() {
        return "MEMO-TEXT";
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return true;
    }

    Merchant getMerchant();

    Currency getCurrency();

    UsdxRestApiService getUsdxRestApiService();

    void checkHeaderOnValidForSecurity(String securityHeaderValue, UsdxTransaction usdxTransaction);

    void createRefillRequestAdmin(Map<String, String> params);

    UsdxTransaction sendUsdxTransactionToExternalWallet(UsdxTxSendAdmin usdxTxSendAdmin);
}
