package com.exrates.inout.service;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;

import java.util.Map;

public interface IRefillable extends IMerchantService {

    Map<String, String> refill(RefillRequestCreateDto request);

    Boolean createdRefillRequestRecordNeeded();

    Boolean needToCreateRefillRequestRecord();

    Boolean generatingAdditionalRefillAddressAvailable();

    Boolean additionalFieldForRefillIsUsed();

    default Boolean storeSameAddressForParentAndTokens() { return false; }

    default String additionalRefillFieldName() {
        return "MEMO";
    }

    default Integer minConfirmationsRefill() {
        return null;
    }

    default boolean concatAdditionalToMainAddress() {
        return false;
    }

    default String getMerchantName(){
        return "Not defined";
    }

    Boolean toMainAccountTransferringConfirmNeeded();

    void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException;
}
