package com.exrates.inout.service.impl;
import com.exrates.inout.configuration.RabbitConfig;
import com.exrates.inout.domain.dto.WalletOperationMsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static com.exrates.inout.configuration.RabbitConfig.REFILL_QUEUE;

@Service
@RequiredArgsConstructor
//@Log4j2
public class RabbitServiceImpl implements RabbitService {

   private static final Logger log = LogManager.getLogger(RabbitServiceImpl.class);

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;


    @Override
    public void send(String route, Object data){
        rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName, route, data);
    }

    @Override
    public void sendAcceptRefillEvent(WalletOperationMsDto dto) {
        try {
            log.info("sendAcceptRefillEvent(): " + dto);
            rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName,REFILL_QUEUE, toJson(dto));
        } catch (JsonProcessingException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        } catch (Exception e){
            log.error(e);
        }
    }

    String toJson(WalletOperationMsDto dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @PostConstruct
    public void test(){
        rabbitTemplate.convertAndSend(RabbitConfig.topicExchangeName,REFILL_QUEUE, new String("Zalupa"));
    }
}
