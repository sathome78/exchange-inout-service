package com.exrates.inout.service.impl;

import com.exrates.inout.configuration.RabbitConfig;
import com.exrates.inout.domain.dto.MerchantOperationDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.IRefillable;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class RabbitMqListener {

    private final ObjectMapper objectMapper;
    private final MerchantServiceContext merchantServiceContext;

    @RabbitListener(queues = RabbitConfig.MERCHANTS_QUEUE)
    public void merchantsListener(org.springframework.amqp.core.Message msg){
        String body = new String(msg.getBody());
        log.info("merchantsListener: " + body);
        try {
            routeToTargetService(body);
        } catch (Exception e){
            log.error(ExceptionUtils.getStackTrace(e));
        }
    }

    private void routeToTargetService(String msg) throws java.io.IOException, RefillRequestAppropriateNotFoundException {
        MerchantOperationDto dto = objectMapper.readValue(msg, MerchantOperationDto.class);
        ((IRefillable)merchantServiceContext.getMerchantService(dto.getMerchantId())).processPayment(dto.getParams());
    }
}
