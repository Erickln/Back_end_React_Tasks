package com.concredito.redis.demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.concredito.redis.demo.config.RabbitMQConfig;

@Component
public class MessageReceiver {

    @RabbitListener(queues = RabbitMQConfig.queueName)
    public void receive(String in) {
        System.out.println("Received: " + in);
    }
}
