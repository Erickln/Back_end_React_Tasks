// UserController.java
package com.concredito.redis.demo.controller;

import com.concredito.redis.demo.config.FileHandler;
import com.concredito.redis.demo.config.MessageSender;
import com.concredito.redis.demo.entity.Task;
import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    MessageSender messageSender;

    @PostMapping
    public ResponseEntity<String> postUser(@RequestBody User user) {

        // Check if user already exists
        String hashedEmail = hashEmail(user.getEmail());
        user.setId(hashedEmail);
        User existingUser = userService.findById(user.getId());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        System.out.println("Creating user: " + user.getId());

        // Create tasks list for user
        user.setTasks(new ArrayList<Task>());

        messageSender.sendUser(user);

        // userService.save(user);

        // Envía un mensaje a RabbitMQ
        // rabbitTemplate.convertAndSend("test-exchange", "foo.bar.baz", "Usuario
        // creado: " + savedUser.getId());
        // messageSender.sendUser(savedUser);

        FileHandler.addStringToFile("User created: " + user.getId());
        return ResponseEntity.ok().body(user.getId());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Enviar un mensaje a RabbitMQ para obtener todos los usuarios
        System.out.println("Calling RabbitMQ to get all users");
        // messageSender.getAll();
        List<User> res = messageSender.getAllUsers();

        FileHandler.addStringToFile("Getting all users");
        return ResponseEntity.ok().body(res);
        // return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        // Envía la solicitud de búsqueda por ID al MessageSender
        User user = messageSender.getAllUsers(userId);
        if (user != null) {
            System.out.println("User found: " + user.getId());
            FileHandler.addStringToFile("User found: " + user.getId());
            return ResponseEntity.ok().body(user);
        } else {
            FileHandler.addStringToFile("User not found: " + userId);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable String id) {
        messageSender.delete(id);

        // if request is successful then return success message
        FileHandler.addStringToFile("User deleted: " + id);
        return ResponseEntity.ok().build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable String id) {
        System.out.println("Updating user: " + user.getId());
        user.setId(id);
        messageSender.patch(user, id);

        FileHandler.addStringToFile("User updated: " + user.getId());
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/{userId}/tasks")
    public ResponseEntity<?> createTaskForUser(@PathVariable String userId, @RequestBody Task task) {
        try {
            // Primero, asegúrate de que el usuario exista
            User user = userService.findById(userId);
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Asigna la tarea al usuario
            messageSender.addTaskToUser(task, userId);
            FileHandler.addStringToFile("Task created: " + task.getId());

            // Retorna una respuesta exitosa
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // En caso de algún error, retorna una respuesta de error
            FileHandler.addStringToFile("Error creating task for user: " + userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar la tarea al usuario.");
        }
    }

    @GetMapping("/{userId}/tasks")
    public ResponseEntity<?> getAllTasksForUser(@PathVariable String userId) {
        try {
            // Primero, asegúrate de que el usuario exista
            User user = userService.findById(userId);
            if (user == null) {
                FileHandler.addStringToFile("User not found: " + userId);
                return ResponseEntity.notFound().build();
            }

            // Obtiene todas las tareas del usuario
            List<Task> tasks = user.getTasks();

            // Retorna la lista de tareas
            FileHandler.addStringToFile("Getting all tasks for user: " + userId);
            return ResponseEntity.ok().body(tasks);
        } catch (Exception e) {
            // En caso de algún error, retorna una respuesta de error
            FileHandler.addStringToFile("Error getting tasks for user: " + userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las tareas del usuario.");
        }
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskForUser(@PathVariable String userId, @PathVariable String taskId) {
        FileHandler.addStringToFile("Deleting task: " + taskId + " for user: " + userId);
        return userService.deleteTask(userId, taskId);
    }

    @SuppressWarnings("null")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String id = hashEmail(user.getEmail());
        ResponseEntity<User> userInDB = getUserById(id);
        User res = userInDB.getBody();
        System.out.println(res.getPassword() + ":" + res.getId());
        FileHandler.addStringToFile("User logged in: " + res.getId());
        return ResponseEntity.ok().body(res.getPassword() + ":" + res.getId());
    }

    @GetMapping("/report")
    public ResponseEntity<?> report() {
        // return report file in root
        return ResponseEntity.ok().body(FileHandler.readFile());

    }

    private String hashEmail(String email) {
        // Lógica para hashear el email y obtener su ID
        String hashedEmail = email.hashCode() + "";
        return hashedEmail;
    }
}
