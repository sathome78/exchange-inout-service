package com.exrates.inout.exceptions;

public class DuplicatedMerchantTransactionIdOrAttemptToRewriteException extends Exception {
    public DuplicatedMerchantTransactionIdOrAttemptToRewriteException(String message) {
        super(message);
    }
}
