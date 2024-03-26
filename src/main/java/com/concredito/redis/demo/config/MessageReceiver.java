// MessageReceiver.java
package com.concredito.redis.demo.config;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;

@Component
public class MessageReceiver {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseSender responseSender;

    @RabbitListener(queues = RabbitMQConfig.queueUser)
    public void receive(String in) {
        System.out.println("Received: " + in);
        // in will have different values based on the request
        // if it starts with "post.user" then it is a request to save a user
        // if it starts with "delete.user" then it is a request to delete a user
        // if it starts with "patch.user" then it is a request to update a user

        ObjectMapper objectMapper = new ObjectMapper();

        if (in.startsWith("post.user.")) {
            try {
                System.out.println("Received request: post.user.");
                User user = objectMapper.readValue(in.substring(10), User.class);
                userService.save(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (in.startsWith("delete.user.")) {
            try {
                String userId = in.substring(12);
                userService.delete(userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (in.startsWith("patch.user.")) {
            // id is after "patch.user." and before the next "{"
            // Recover string after "patch.user." and before the next "{"
            String id = in.substring(11, in.indexOf("{") - 1);

            // Recover the user object

            String userString = in.substring(11 + id.length() + 1);

            System.out.println("User string: " + userString);

            try {
                User user = objectMapper.readValue(userString, User.class);
                userService.patch(user, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @RabbitListener(queues = RabbitMQConfig.queueGetAll)
    public void receiveGetAll() {
        System.out.println("Received request: getAll");
        List<User> users = userService.findAll();
        System.out.println("Users found: " + users.size());
        ObjectMapper objectMapper = new ObjectMapper();
        String response = null;
        try {
            response = objectMapper.writeValueAsString(users);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseSender.sendResponseForGetAll(response);
    }

    @RabbitListener(queues = RabbitMQConfig.queueFindUserById)
    public void receiveFindUserById(String userId) {
        System.out.println("Received request: findUserById with id: " + userId);
        User user = userService.findById(userId);
        System.out.println("User found: " + user);
        ObjectMapper objectMapper = new ObjectMapper();
        String response = null;
        try {
            response = objectMapper.writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        responseSender.sendResponseForFindUserById(response);
    }
}
