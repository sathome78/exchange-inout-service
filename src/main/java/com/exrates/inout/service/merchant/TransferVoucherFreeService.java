package com.exrates.inout.service.merchant;

import com.exrates.inout.domain.enums.TransferProcessTypeEnum;
import com.exrates.inout.service.ITransferable;

public interface TransferVoucherFreeService extends ITransferable {

  @Override
  default public Boolean isVoucher() {
    return true;
  }

  @Override
  default public Boolean recipientUserIsNeeded() {
    return false;
  }

  @Override
  default public TransferProcessTypeEnum processType() {
    return TransferProcessTypeEnum.VOUCHER_FREE;
  }

}
