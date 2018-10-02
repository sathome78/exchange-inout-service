package com.exrates.inout.exceptions;

/**
 * Created by ValkSam
 */
public class DuplicatedMerchantTransactionIdOrAttemptToRewriteException extends Exception {
    public DuplicatedMerchantTransactionIdOrAttemptToRewriteException(String message) {
        super(message);
    }
}
