// MessageSender.java
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
    private RabbitMQConfig rabbitMQConfig;

    @SuppressWarnings("static-access")
    public void send(String message) {
        rabbitTemplate.convertAndSend(rabbitMQConfig.topicExchangeUser, "foo.bar.baz", message);
    }

    public void sendUser(Object user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String userJson = objectMapper.writeValueAsString(user);
            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz", "post.user." + userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "unchecked", "static-access" })
    public List<User> getAllUsers() {
        System.out.println("Sending message to RabbitMQ to get all users");
        rabbitTemplate.convertAndSend(rabbitMQConfig.topicExchangeGetAll, "foo.bar.baz", "get.all.users");

        String response = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.queueResponseGetAll);

        List<User> users = null;
        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                users = objectMapper.readValue(response, List.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    @SuppressWarnings("static-access")
    public User findUserById(String userId) {
        System.out.println("Sending message to RabbitMQ to find user by id: " + userId);
        rabbitTemplate.convertAndSend(rabbitMQConfig.topicExchangeFindUserById, "find-user-by-id", userId);

        String response = (String) rabbitTemplate.receiveAndConvert(rabbitMQConfig.queueUserIdResponse);

        User user = null;
        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                user = objectMapper.readValue(response, User.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public void delete(String id) {
        rabbitTemplate.convertAndSend(rabbitMQConfig.topicExchangeUser, "foo.bar.baz", "delete.user." + id);
    }

    public void patch(User user, String id) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String userJson = objectMapper.writeValueAsString(user);
            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz",
                    "patch.user." + id + userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
