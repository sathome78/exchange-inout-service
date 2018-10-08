package com.exrates.inout.service.tron;

import com.exrates.inout.domain.dto.TronNewAddressDto;
import com.exrates.inout.domain.dto.TronTransferDto;
import org.json.JSONObject;

public interface TronNodeService {

    TronNewAddressDto getNewAddress();

    JSONObject transferFunds(TronTransferDto tronTransferDto);

    JSONObject getTransaction(String hash);

    JSONObject getAccount(String addressBase58);
}
