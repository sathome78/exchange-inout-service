package com.exrates.inout.service.waves;

//exrates.model.dto.merchants.waves.WavesPayment;
//exrates.model.dto.merchants.waves.WavesTransaction;

import com.exrates.inout.domain.waves.WavesPayment;
import com.exrates.inout.domain.waves.WavesTransaction;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface WavesRestClient {
    void init(Properties props);

    String generateNewAddress();

    Integer getCurrentBlockHeight();

    String transferCosts(WavesPayment payment);

    List<WavesTransaction> getTransactionsForAddress(String address);

    Optional<com.exrates.inout.domain.waves.WavesTransaction> getTransactionById(String id);

    Long getAccountWavesBalance(String account);
}
