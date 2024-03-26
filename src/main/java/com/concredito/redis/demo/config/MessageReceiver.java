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
        // parse in to user object
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            User user = objectMapper.readValue(in, User.class);
            // System.out.println("Userid: " + user.getId() + "email: " + user.getEmail() +
            // "password: "
            // + user.getPassword() + "admin:" + user.isAdmin());
            // save user
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.queueGetAll)
    public void receiveGetAll() {
        System.out.println("Received request: getAll");
        List<User> users = userService.findAll();

        System.out.println("Users found: " + users.size());

        // Convert the list of users to JSON string
        ObjectMapper objectMapper = new ObjectMapper();
        String response = null;
        try {
            response = objectMapper.writeValueAsString(users);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send the response back to the sender
        responseSender.sendResponse(response);
    }
}
