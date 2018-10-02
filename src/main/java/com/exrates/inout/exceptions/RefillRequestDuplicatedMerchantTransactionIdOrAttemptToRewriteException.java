package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException extends RuntimeException {
    public RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException(String message) {
        super(message);
    }
}
