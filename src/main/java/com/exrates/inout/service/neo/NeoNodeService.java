package com.exrates.inout.service.neo;

import com.exrates.inout.domain.neo.Block;
import com.exrates.inout.domain.neo.NeoAsset;
import com.exrates.inout.domain.neo.NeoTransaction;

import java.math.BigDecimal;
import java.util.Optional;

public interface NeoNodeService {
    String getNewAddress();

    Integer getBlockCount();

    Optional<Block> getBlock(Integer height);

    Optional<NeoTransaction> getTransactionById(String txId);

    NeoTransaction sendToAddress(NeoAsset asset, String address, BigDecimal amount, String changeAddress);
}
