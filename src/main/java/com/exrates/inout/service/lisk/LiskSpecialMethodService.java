package com.exrates.inout.service.lisk;

import com.exrates.inout.domain.lisk.LiskAccount;

public interface LiskSpecialMethodService {

    String sendTransaction(String secret, Long amount, String recipientId);

    LiskAccount createAccount(String secret);
}
