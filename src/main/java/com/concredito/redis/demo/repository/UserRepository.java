// UserRepository.java
package com.concredito.redis.demo.repository;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.concredito.redis.demo.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Repository
public class UserRepository {

    public static final String HASH_KEY = "User";

    private final HashOperations<String, String, User> hashOperations;

    public UserRepository(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @SuppressWarnings("null")
    public User save(User user) {
        hashOperations.put(HASH_KEY, user.getId(), user);
        return user;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<User> findAll() {
        return (List<User>) (List) hashOperations.values(HASH_KEY);

    }

    @SuppressWarnings("null")
    public User findById(String id) {
        return (User) hashOperations.get(HASH_KEY, id);
    }

    public String delete(String id) {
        hashOperations.delete(HASH_KEY, id);
        return "User removed !!";
    }

    public User patch(User user, String id) {
        System.out.println("UserRepository: patch");
        hashOperations.put(HASH_KEY, id, user);
        return user;
    }
}
