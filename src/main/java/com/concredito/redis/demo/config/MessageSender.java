// MessageSender.java
package com.concredito.redis.demo.config;

import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.concredito.redis.demo.entity.Task;
import com.concredito.redis.demo.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
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

    public List<User> getAllUsers() {
        System.out.println("Sending message to RabbitMQ to get all users");
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeGetAll, "foo.bar.baz", "get.all.users");

        String response = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.queueResponseGetAll);

        List<User> users = null;
        if (response != null) {
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

    public User getAllUsers(String userId) {
        System.out.println("Sending message to RabbitMQ to get all users");
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeGetAll, "foo.bar.baz", "get.all.users");

        String response = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.queueResponseGetAll);

        List<User> users = null;
        User user = null;
        if (response != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                users = objectMapper.readValue(response, new TypeReference<List<User>>() {
                });
                user = users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);

                System.out.println("User found 5555555555555555555: " + user);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return user;
    }

    public User findUserById(String userId) {
        System.out.println("Sending message to RabbitMQ to find user by id: " + userId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeFindUserById, "find-user-by-id", userId);

        String response = (String) rabbitTemplate.receiveAndConvert(RabbitMQConfig.queueUserIdResponse);

        System.out.println("Response from RabbitMQ: " + response);

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
        rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz", "delete.user." + id);
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

    public ResponseEntity<Task> addTaskToUser(Task task, String userId) {
        System.out.println("Sending message to RabbitMQ to create task for user: " + userId);

        // parse task to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = null;
        try {
            taskJson = objectMapper.writeValueAsString(task);
            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangeUser, "foo.bar.baz",
                    "post.task." + userId + taskJson);
            return ResponseEntity.ok().body(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
