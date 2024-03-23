package com.concredito.redis.demo.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Repository;

import com.concredito.redis.demo.entity.Task;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    // para acceder a las operaciones de hash en Redis
    private final HashOperations<String, String, Task> hashOperations;

    public TaskRepository(RedisTemplate<String, Object> redisTemplate) {
        this.hashOperations = redisTemplate.opsForHash();
    }

    private String getKey(String userId) {
        return "user:" + userId + ":tasks";
    }

    @SuppressWarnings("null")
    public void saveTask(String userId, Task task) {
        hashOperations.put(getKey(userId), task.getId(), task);
    }

    @SuppressWarnings("null")
    public Task findTaskById(String userId, String taskId) {
        return (Task) hashOperations.get(getKey(userId), taskId);
    }

    @SuppressWarnings("null")
    public List<Task> findAllTasksByUserId(String userId) {
        return (List<Task>) hashOperations.values(getKey(userId)).stream().map(task -> (Task) task)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    public void deleteTask(String userId, String taskId) {
        hashOperations.delete(getKey(userId), taskId);
    }
}
