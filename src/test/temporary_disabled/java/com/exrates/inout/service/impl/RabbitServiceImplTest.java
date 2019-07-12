package com.exrates.inout.service.impl;

import com.exrates.inout.domain.dto.WalletOperationMsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.other.WalletOperationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.math.BigDecimal;

public class RabbitServiceImplTest {

    @Test
    public void toJson() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        RabbitServiceImpl rabbitService = new RabbitServiceImpl(null, mapper);
        WalletOperationData walletOperationData = new WalletOperationData();
        walletOperationData.setAmount(new BigDecimal(5));
        walletOperationData.setOperationType(OperationType.INPUT);
        WalletOperationMsDto dto = new WalletOperationMsDto(walletOperationData, 5);
        String json = rabbitService.toJson(dto);

        WalletOperationMsDto fromJson = mapper.readValue(json, WalletOperationMsDto.class);

        assert fromJson.equals(dto);
    }
}