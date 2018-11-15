package com.exrates.inout.service.transfers;

import com.exrates.inout.domain.enums.TransferProcessTypeEnum;
import com.exrates.inout.service.ITransferable;

public interface TransferVoucherService extends ITransferable {

  @Override
  default public Boolean isVoucher() {
    return true;
  }

  @Override
  default public Boolean recipientUserIsNeeded() {
    return true;
  }

  @Override
  default public TransferProcessTypeEnum processType() {
    return TransferProcessTypeEnum.VOUCHER;
  }

}
