package com.exrates.inout.service.waves;

import com.exrates.inout.domain.WavesPayment;
import com.exrates.inout.domain.dto.waves.WavesTransaction;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface WavesRestClient {
    void init(Properties props);

    String generateNewAddress();

    Integer getCurrentBlockHeight();

    String transferCosts(WavesPayment payment);

    List<WavesTransaction> getTransactionsForAddress(String address);

    Optional<WavesTransaction> getTransactionById(String id);
}
