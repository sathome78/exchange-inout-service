package com.exrates.inout.service.omni;

//exrates.model.Currency;
//exrates.model.Merchant;
//exrates.model.RefillRequestAddressShortDto;
//exrates.model.dto.RefillRequestAcceptDto;
//exrates.model.dto.RefillRequestPutOnBchExamDto;
//exrates.model.dto.merchants.omni.OmniBalanceDto;
//exrates.model.dto.merchants.omni.OmniTxDto;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestPutOnBchExamDto;
import com.exrates.inout.domain.dto.omni.OmniBalanceDto;
import com.exrates.inout.domain.dto.omni.OmniTxDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.RefillRequestAddressShortDto;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


public interface OmniService extends IRefillable, IWithdrawable {

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

    void putOnBchExam(RefillRequestPutOnBchExamDto dto);

    RefillRequestAcceptDto createRequest(String address, String hash, BigDecimal amount);

    void frozeCoins(String address, BigDecimal amount);

    Merchant getMerchant();

    Currency getCurrency();

    String getWalletPassword();

    OmniBalanceDto getUsdtBalances();

    BigDecimal getBtcBalance();

    Integer getUsdtPropertyId();

    List<OmniTxDto> getAllTransactions();

    List<RefillRequestAddressShortDto> getBlockedAddressesOmni();

    void createRefillRequestAdmin(Map<String, String> params);
}
