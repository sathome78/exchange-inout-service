package com.exrates.inout.exceptions;

public class RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException extends RuntimeException {
    public RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException(String message) {
        super(message);
    }
}
