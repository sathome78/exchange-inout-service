package com.exrates.inout.service.merchantStrategy;

import me.exrates.model.dto.WithdrawMerchantOperationDto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by ValkSam on 24.03.2017.
 */
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
