package com.exrates.inout.service.impl;

import com.exrates.inout.configuration.RabbitConfig;
import com.exrates.inout.domain.other.WalletOperationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RabbitServiceImpl {

    private static final String REFILL_ROUTE = "refill";
    private final RabbitTemplate rabbitTemplate;

    public RabbitServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(String route, Object data){
        rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, route, data);
    }

    public void sendAcceptRefillEvent(WalletOperationData walletOperationData) {
        try {
            rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, REFILL_ROUTE, new ObjectMapper().writeValueAsString(walletOperationData));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
