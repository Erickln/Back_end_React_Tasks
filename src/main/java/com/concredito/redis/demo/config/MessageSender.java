package com.concredito.redis.demo.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeName, "foo.bar.baz", message);
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("test-exchange", "foo.bar.baz", message);
    }
}
