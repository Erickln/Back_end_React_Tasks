package com.concredito.redis.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class ResponseSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendResponse(String response, String exchangeName) {
        rabbitTemplate.convertAndSend(exchangeName, "foo.bar.baz", response);
    }

    public void sendResponseForGetAll(String response) {
        sendResponse(response, RabbitMQConfig.topicExchangResponseGetAll);
    }

    public void sendResponseForFindUserById(String response) {
        System.out.println("Sending response for find user by id");
        sendResponse(response, RabbitMQConfig.topicExchangeFindUserById);
    }

}
