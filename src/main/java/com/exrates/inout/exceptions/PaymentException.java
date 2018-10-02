package com.exrates.inout.exceptions;

import com.exrates.inout.domain.enums.WalletTransferStatus;

public class PaymentException extends RuntimeException {
    public PaymentException(WalletTransferStatus walletTransferStatus) {
    }
}
