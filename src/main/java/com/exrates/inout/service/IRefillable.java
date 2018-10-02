package com.exrates.inout.service;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;

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
}
