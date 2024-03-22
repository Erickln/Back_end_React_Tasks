// UserController.java
package com.concredito.redis.demo.controller;

import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public User save(@RequestBody User user) {
        String hashedEmail = hashEmail(user.getEmail()); // Suponiendo que tienes una función para hashear el email
        user.setId(hashedEmail);
        return userService.save(user);
    }

    @GetMapping
    public List<User> list() {
        return userService.findAll();
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
