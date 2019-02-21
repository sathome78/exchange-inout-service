package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.TronNewAddressDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import lombok.SneakyThrows;
import org.json.JSONObject;

//exrates.model.dto.TronNewAddressDto;
//exrates.model.dto.TronTransferDto;

public interface TronNodeService {

    TronNewAddressDto getNewAddress();

    JSONObject transferFunds(TronTransferDto tronTransferDto);

    @SneakyThrows
    JSONObject transferAsset(TronTransferDto tronTransferDto);

    JSONObject getTransactions(long blockNum);

    JSONObject getTransaction(String hash);

    JSONObject getLastBlock();

    JSONObject getAccount(String addressBase58);
}
