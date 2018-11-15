package com.exrates.inout.service;


import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;

import java.math.BigDecimal;
import java.util.Map;

public interface IWithdrawable extends IMerchantService {

    Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception;

    Boolean additionalTagForWithdrawAddressIsUsed();

    Boolean withdrawTransferringConfirmNeeded();

    default String additionalWithdrawFieldName() {
        return "MEMO";
    }


    default boolean specificWithdrawMerchantCommissionCountNeeded() {
        return false;
    }

    default BigDecimal countSpecCommission(BigDecimal amount, String destinationTag, Integer merchantId) {
        return BigDecimal.ZERO;
    }

    default void checkDestinationTag(String destinationTag) {}

    default boolean comissionDependsOnDestinationTag() {
        return false;
    }

    boolean isValidDestinationAddress(String address);
}
