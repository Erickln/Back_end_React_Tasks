package com.concredito.redis.demo.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Task")
@Setter
@Getter
public class Task implements Serializable {

    @Id
    private String id; // Unique identifier for the task
    private String name;
    private String description; // Added description field
    private boolean completed; // Tracks whether the task is completed

    // Consider adding more fields as needed, for example, dueDate, priority, etc.
}
