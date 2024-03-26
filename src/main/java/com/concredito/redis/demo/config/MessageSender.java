package com.concredito.redis.demo.config;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import com.concredito.redis.demo.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.CountDownLatch;

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
        // Envía un mensaje a la cola de RabbitMQ solicitando todos los usuarios
        System.out.println("Sending message to RabbitMQ to get all users");
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeGetAll, "foo.bar.baz", "get.all.users");

        // Espera la respuesta de RabbitMQ
        String response = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.queueResponseGetAll);

        // Procesa la respuesta
        List<User> users = null;
        if (response != null) {
            // Convierte la respuesta JSON a una lista de usuarios
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                users = objectMapper.readValue(response, new TypeReference<List<User>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

}
