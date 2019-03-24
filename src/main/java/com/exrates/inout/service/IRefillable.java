package com.exrates.inout.service;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.exceptions.NotImplementedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.util.Map;

public interface IRefillable extends IMerchantService{

    Map<String, String> refill(RefillRequestCreateDto request);

    void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException;

    Boolean createdRefillRequestRecordNeeded();

    Boolean needToCreateRefillRequestRecord();

    Boolean toMainAccountTransferringConfirmNeeded();

    Boolean generatingAdditionalRefillAddressAvailable();

    Boolean additionalFieldForRefillIsUsed();

    default Boolean storeSameAddressForParentAndTokens() {
        return false;
    };

    default String additionalRefillFieldName() {
        return "MEMO";
    };

    default Integer minConfirmationsRefill() {
        return null;
    };

    default boolean concatAdditionalToMainAddress() { return false; }

    default String getMerchantName(){
        return "Not defined";
    }

    default long getBlocksCount() throws BitcoindException, CommunicationException {
        throw new NotImplementedMethod("Not implemented yet");
    }

    default Long getLastBlockTime() throws BitcoindException, CommunicationException, BitcoindException, CommunicationException, BitcoindException {
        throw new NotImplementedMethod("Not implemented yet");
    }

}