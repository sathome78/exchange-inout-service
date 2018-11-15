package com.exrates.inout.service.nem;

import com.exrates.inout.domain.dto.MosaicIdDto;
import com.exrates.inout.domain.dto.NemMosaicTransferDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import lombok.Synchronized;
import org.nem.core.model.Account;

import java.util.List;
import java.util.Map;


public interface NemService extends IRefillable, IWithdrawable {

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

    Account getAccount();

    @Override
    default String additionalRefillFieldName() {
        return "Message";
    }

    @Override
    default String additionalWithdrawFieldName() {
        return "Message";
    }

    @Override
    default boolean comissionDependsOnDestinationTag() {
        return true;
    }

    @Override
    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return true;
    }

    void processMosaicPayment(List<NemMosaicTransferDto> mosaics, Map<String, String> params);

    void checkRecievedTransaction(RefillRequestFlatDto dto) throws RefillRequestAppropriateNotFoundException;

    boolean checkSendedTransaction(String hash, String additionalParams);

    List<MosaicIdDto> getDeniedMosaicList();
}
