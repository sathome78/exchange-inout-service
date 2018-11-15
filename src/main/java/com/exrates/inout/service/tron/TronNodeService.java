package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.TronNewAddressDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import lombok.SneakyThrows;
import org.json.JSONObject;

public interface TronNodeService {

    TronNewAddressDto getNewAddress();

    JSONObject transferFunds(TronTransferDto tronTransferDto);

    @SneakyThrows
    JSONObject getTransactions(long blockNum);

    JSONObject getTransaction(String hash);

    @SneakyThrows
    JSONObject getLastBlock();

    JSONObject getAccount(String addressBase58);
}
