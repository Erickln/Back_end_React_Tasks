// UserService.java
package com.concredito.redis.demo.service;

import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id);
    }

    public String delete(String id) {
        return userRepository.delete(id);
    }

    public User patch(User user) {
        return userRepository.patch(user);
    }
}
