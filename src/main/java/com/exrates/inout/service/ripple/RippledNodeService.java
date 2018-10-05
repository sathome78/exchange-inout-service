package com.exrates.inout.service.ripple;

import com.exrates.inout.domain.dto.RippleAccount;
import com.exrates.inout.domain.dto.RippleTransaction;
import org.json.JSONObject;

public interface RippledNodeService {

    void signTransaction(RippleTransaction transaction);

    void submitTransaction(RippleTransaction transaction);

    JSONObject getTransaction(String txHash);

    JSONObject getAccountInfo(String accountName);

    RippleAccount porposeAccount();

    JSONObject getServerState();
}