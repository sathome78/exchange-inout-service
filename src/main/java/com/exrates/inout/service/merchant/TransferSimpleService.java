package com.exrates.inout.service.merchant;

import com.exrates.inout.domain.enums.TransferProcessTypeEnum;
import com.exrates.inout.service.ITransferable;

public interface TransferSimpleService extends ITransferable {

  @Override
  default public Boolean isVoucher() {
    return false;
  }

  @Override
  default public Boolean recipientUserIsNeeded() {
    return true;
  }

  @Override
  default public TransferProcessTypeEnum processType(){
    return TransferProcessTypeEnum.TRANSFER;
  }
}
