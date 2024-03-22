// UserRepository.java
package com.concredito.redis.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.concredito.redis.demo.entity.User;
import java.util.List;

@Repository
public class UserRepository {

    public static final String HASH_KEY = "User";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("null")
    public User save(User user) {
        redisTemplate.opsForHash().put(HASH_KEY, user.getId(), user);
        return user;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<User> findAll() {
        return (List<User>) (List) redisTemplate.opsForHash().values(HASH_KEY);
    }

    @SuppressWarnings("null")
    public User findById(String id) {
        return (User) redisTemplate.opsForHash().get(HASH_KEY, id);
    }

    public String delete(String id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
        return "User removed !!";
    }

    public User patch(User user) {
        return save(user);
    }
}
