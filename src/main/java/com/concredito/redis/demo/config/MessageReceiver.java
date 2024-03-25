package com.concredito.redis.demo.config;

import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;

@Component
public class MessageReceiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UserService userService;

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
    public void receiveAll() {
        System.out.println("Received: getAll");
        List<User> users = userService.findAll();
        // Convert list of users to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String usersJson = objectMapper.writeValueAsString(users);
            // Send the JSON response back to the sender
            rabbitTemplate.convertAndSend(RabbitMQConfig.topicExchangResponseGetAll, "foo.bar.baz", usersJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
