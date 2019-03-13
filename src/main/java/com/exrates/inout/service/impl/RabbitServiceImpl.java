package com.exrates.inout.service.impl;

import com.exrates.inout.configuration.RabbitConfig;
import com.exrates.inout.domain.dto.WalletOperationMsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitServiceImpl implements RabbitService {

    private static final String REFILL_ROUTE = "refill";
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void send(String route, Object data){
        rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, route, data);
    }

    @Override
    public void sendAcceptRefillEvent(WalletOperationMsDto dto) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, REFILL_ROUTE, toJson(dto));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    String toJson(WalletOperationMsDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }
}
