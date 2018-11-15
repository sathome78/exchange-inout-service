package com.exrates.inout.service.ripple;

import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;

public interface RippleService extends IRefillable, IWithdrawable {

    /*return: true if tx validated; false if not validated but validation in process,
    throws Exception if declined*/
    boolean checkSendedTransaction(String hash, String additionalParams);

    void onTransactionReceive(String hash, Integer destinationTag, String amount);

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
    return true;
  }

  @Override
  default String additionalWithdrawFieldName() {
    return "Destination Tag";
  }

  @Override
  default String additionalRefillFieldName() {
    return "Destination Tag";
  }
}
