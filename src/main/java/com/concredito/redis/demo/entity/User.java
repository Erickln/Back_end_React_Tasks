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
@Setter
@Getter
@RedisHash("User")
public class User implements Serializable {

    @Id
    private String id;
    private String email;
    private String password;
    private boolean admin;

    public void setId(String email) {
        this.id = email;
    }
}
