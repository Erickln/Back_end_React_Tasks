// UserRepository.java
package com.concredito.redis.demo.repository;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.concredito.redis.demo.entity.Task;
import com.concredito.redis.demo.entity.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    public static final String HASH_KEY = "User";
    public static final String HASH_KEY_TASK = "Task";

    private final HashOperations<String, String, User> hashOperations;

    public UserRepository(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    @SuppressWarnings("null")
    public User save(User user) {
        if (user.getTasks() == null) {
            user.setTasks(new ArrayList<Task>());
        }
        System.out.println("UserRepository: save");
        hashOperations.put(HASH_KEY, user.getId(), user);
        return user;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<User> findAll() {
        System.out.println(hashOperations.values(HASH_KEY_TASK));
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

    @SuppressWarnings("null")
    public User patch(User user, String id) {
        System.out.println("UserRepository: patch");
        System.out.println(user);
        System.out.println(id);
        user.setId(id);
        System.out.println(user);
        System.out.println(id);
        hashOperations.put(HASH_KEY, id, user);
        return user;
    }

    public Task saveTask(Task task, String userId) {
        User user = findById(userId);
        // check if id is already present

        if (user != null) {
            System.out.println(user);
            System.out.println(user.getTasks());
            // create List of tasks if it is null
            user.getTasks().add(task);
            System.out.println(user.getTasks());
            save(user);
            return task;
        }
        throw new RuntimeException("User not found !!");
    }

    public ResponseEntity<?> deleteTask(String userId, String taskId) {
        User user = findById(userId);
        if (user != null) {
            List<Task> tasks = user.getTasks();
            for (Task task : tasks) {
                if (task.getId().equals(taskId)) {
                    tasks.remove(task);
                    save(user);
                    return ResponseEntity.ok().build();
                }
            }
        }
        return ResponseEntity.notFound().build();
    }
}
