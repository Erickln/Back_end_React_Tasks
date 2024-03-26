package com.concredito.redis.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Configuration
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestResponseMessage {
    private String request;
    private String response;

}
