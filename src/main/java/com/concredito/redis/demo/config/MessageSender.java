package com.concredito.redis.demo.config;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concredito.redis.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MessageSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQConfig RabbitMQConfig;

    @SuppressWarnings("static-access")
    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz", message);
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend("test-exchange", "foo.bar.baz", message);
    }

    @SuppressWarnings("static-access")
    public void sendUser(Object user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String userJson = objectMapper.writeValueAsString(user);
            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz", userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("static-access")
    public List<User> getAll() {
        // Envía un mensaje a la cola de RabbitMQ con la solicitud
        System.out.println("Sending message to RabbitMQ");
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeGetAll, "foo.bar.baz", "get-all-users");
        System.out.println("Message sent");

        return null;
    }

}
