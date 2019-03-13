package com.exrates.inout.service.impl;

import com.exrates.inout.domain.dto.WalletOperationMsDto;

public interface RabbitService {
    void send(String route, Object data);

    void sendAcceptRefillEvent(WalletOperationMsDto dto);
}
