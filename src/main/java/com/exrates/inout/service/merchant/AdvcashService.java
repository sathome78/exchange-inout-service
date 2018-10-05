package com.exrates.inout.service.merchant;

import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import org.springframework.stereotype.Service;

@Service
public interface AdvcashService extends IRefillable, IWithdrawable {

  @Override
  default Boolean createdRefillRequestRecordNeeded() {
    return true;
  }

  @Override
  default Boolean needToCreateRefillRequestRecord() {
    return true;
  }

  @Override
  default Boolean toMainAccountTransferringConfirmNeeded() {
    return false;
  }

  @Override
  default Boolean generatingAdditionalRefillAddressAvailable() {
    return null;
  }

  @Override
  default Boolean additionalTagForWithdrawAddressIsUsed() {
    return false;
  }

  @Override
  default Boolean additionalFieldForRefillIsUsed() {
    return false;
  }

  @Override
  default Boolean withdrawTransferringConfirmNeeded() {
    return false;
  }
}
