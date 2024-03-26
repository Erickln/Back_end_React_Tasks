// UserController.java
package com.concredito.redis.demo.controller;

import com.concredito.redis.demo.config.MessageSender;
import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    MessageSender messageSender;

    @PostMapping
    public User save(@RequestBody User user) {
        String hashedEmail = hashEmail(user.getEmail());
        user.setId(hashedEmail);
        User savedUser = userService.save(user);

        // Envía un mensaje a RabbitMQ
        // rabbitTemplate.convertAndSend("test-exchange", "foo.bar.baz", "Usuario
        // creado: " + savedUser.getId());
        // messageSender.sendUser(savedUser);
        return savedUser;
    }

    @GetMapping
    public List<User> list() {
        // Enviar un mensaje a RabbitMQ para obtener todos los usuarios
        System.out.println("Calling RabbitMQ to get all users");
        // messageSender.getAll();
        List<User> res = messageSender.getAll();

        return res;
        // return null;
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        return userService.delete(id);
    }

    @PatchMapping
    public User update(@RequestBody User user) {
        return userService.patch(user);
    }

    private String hashEmail(String email) {
        // Lógica para hashear el email y obtener su ID
        String hashedEmail = email.hashCode() + "";
        String finalHash = UUID.randomUUID().toString() + Math.abs(hashedEmail.hashCode());
        return finalHash;
    }
}
