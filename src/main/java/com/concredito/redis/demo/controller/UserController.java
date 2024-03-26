// UserController.java
package com.concredito.redis.demo.controller;

import com.concredito.redis.demo.config.MessageSender;
import com.concredito.redis.demo.entity.Task;
import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    MessageSender messageSender;

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user) {
        String hashedEmail = hashEmail(user.getEmail());
        user.setId(hashedEmail);

        // Create tasks list for user
        user.setTasks(new ArrayList<Task>());

        messageSender.sendUser(user);

        // Envía un mensaje a RabbitMQ
        // rabbitTemplate.convertAndSend("test-exchange", "foo.bar.baz", "Usuario
        // creado: " + savedUser.getId());
        // messageSender.sendUser(savedUser);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        // Enviar un mensaje a RabbitMQ para obtener todos los usuarios
        System.out.println("Calling RabbitMQ to get all users");
        // messageSender.getAll();
        List<User> res = messageSender.getAllUsers();

        return ResponseEntity.ok().body(res);
        // return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        // Envía la solicitud de búsqueda por ID al MessageSender
        User user = messageSender.getAllUsers(userId);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable String id) {
        messageSender.delete(id);

        // if request is successful then return success message
        return ResponseEntity.ok().build();

    }

    @PatchMapping("/{id}")
    public ResponseEntity<User> update(@RequestBody User user, @PathVariable String id) {
        System.out.println("Updating user: " + user.getId());
        user.setId(id);
        messageSender.patch(user, id);

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

            // Retorna una respuesta exitosa
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // En caso de algún error, retorna una respuesta de error
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
                return ResponseEntity.notFound().build();
            }

            // Obtiene todas las tareas del usuario
            List<Task> tasks = user.getTasks();

            // Retorna la lista de tareas
            return ResponseEntity.ok().body(tasks);
        } catch (Exception e) {
            // En caso de algún error, retorna una respuesta de error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las tareas del usuario.");
        }
    }

    @DeleteMapping("/{userId}/tasks/{taskId}")
    public ResponseEntity<?> deleteTaskForUser(@PathVariable String userId, @PathVariable String taskId) {
        return userService.deleteTask(userId, taskId);
    }

    private String hashEmail(String email) {
        // Lógica para hashear el email y obtener su ID
        String hashedEmail = email.hashCode() + "";
        String finalHash = UUID.randomUUID().toString() + Math.abs(hashedEmail.hashCode());
        return finalHash;
    }
}
