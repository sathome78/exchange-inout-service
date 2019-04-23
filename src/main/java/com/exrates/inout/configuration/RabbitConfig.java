package com.exrates.inout.configuration;

import com.exrates.inout.TestRabbitReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitConfig  {

    public static final String topicExchangeName = "inout-service";
    public static final String REFILL_QUEUE = "refill";


    @Bean
    Queue queue() {
        return new Queue(REFILL_QUEUE, true);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(REFILL_QUEUE);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(TestRabbitReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


}
