package com.exrates.inout.service.aidos;

import com.exrates.inout.domain.dto.BtcTransactionDto;
import com.exrates.inout.domain.dto.BtcWalletPaymentItemDto;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;

public interface AidosNodeService {
    String generateNewAddress();

    BigDecimal getBalance();

    BtcTransactionDto getTransaction(String txId);

    JSONArray getAllTransactions(Integer count, Integer from);

    JSONObject sendToAddress(String address, BigDecimal amount);


    JSONObject sendMany(List<BtcWalletPaymentItemDto> payments);

    boolean unlockWallet(String pass, int seconds);

    JSONArray getAllTransactions();
}
