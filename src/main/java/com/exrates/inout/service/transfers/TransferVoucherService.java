package com.exrates.inout.service.transfers;

import com.exrates.inout.domain.enums.TransferProcessTypeEnum;
import com.exrates.inout.service.ITransferable;

public interface TransferVoucherService extends ITransferable {

    @Override
    default Boolean isVoucher() {
        return true;
    }

    @Override
    default Boolean recipientUserIsNeeded() {
        return true;
    }

    @Override
    default TransferProcessTypeEnum processType() {
        return TransferProcessTypeEnum.VOUCHER;
    }
}
