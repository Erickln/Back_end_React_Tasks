package com.concredito.redis.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class ResponseSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig rabbitMQConfig;

    public void sendResponse(String response) {
        rabbitTemplate.convertAndSend(rabbitMQConfig.topicExchangResponseGetAll, "foo.bar.baz", response);
    }
}
