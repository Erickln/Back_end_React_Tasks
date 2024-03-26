// UserService.java
package com.concredito.redis.demo.service;

import com.concredito.redis.demo.entity.Task;
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
        List<User> users = userRepository.findAll();
        System.out.println("Sending all users to client ontoi");

        return users;

        // return userRepository.findAll();
    }

    public User findById(String id) {
        System.out.println("Sending user by id to client ontoi");
        return userRepository.findById(id);
    }

    public String delete(String id) {
        return userRepository.delete(id);
    }

    public User patch(User user, String id) {
        return userRepository.patch(user, id);
    }

    public Task addTask(String userId, Task task) {
        User user = findById(userId);
        if (user != null) {
            user.getTasks().add(task);
            save(user);

            //
            return task;
        }
        throw new RuntimeException("User not found !!");
    }

    public List<Task> getAllTasks(String userId) {
        User user = findById(userId);
        return (user != null) ? user.getTasks() : null;
    }

    public Task getTaskById(String userId, String taskId) {
        User user = findById(userId);
        if (user != null) {
            for (Task task : user.getTasks()) {
                if (taskId.equals(task.getId())) {
                    return task;
                }
            }
        }
        return null;
    }

    public String deleteTask(String userId, String taskId) {
        User user = findById(userId);
        if (user != null) {
            List<Task> tasks = user.getTasks();
            for (Task task : tasks) {
                if (taskId.equals(task.getId())) {
                    tasks.remove(task);
                    save(user);
                    return "Task removed !!";
                }
            }
        }
        return "Task not found !!";
    }
}
