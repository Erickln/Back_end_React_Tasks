package com.concredito.redis.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Define los nombres de los intercambios y colas
    public static final String topicExchangeUser = "user-exchange";
    public static final String queueUser = "user-queue";
    public static final String topicExchangeGetAll = "get-all-exchange";
    public static final String queueGetAll = "get-all-queue";
    public static final String topicExchangResponseGetAll = "response-get-all-users";
    public static final String queueResponseGetAll = "response-get-all-users-queue";
    public static final String queueFindUserById = "find-user-by-id-queue";
    public static final String topicExchangeFindUserById = "find-user-by-id-exchange";
    public static final String queueUserIdResponse = "user-id-response-queue";
    public static final String topicExchangeUserResponse = "user-exchange-response";

    // Define la cola principal y el intercambio principal
    @Bean
    Queue queue() {
        return new Queue(queueUser, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeUser);
    }

    // Define la cola 'get.all.users' y el intercambio 'get.all.users'
    @Bean
    Queue getAllUsersQueue() {
        return new Queue(queueGetAll, false);
    }

    @Bean
    TopicExchange getAllUsersExchange() {
        return new TopicExchange(topicExchangeGetAll);
    }

    // Define la cola 'response.get.all.users' y la cola 'response.get.all.users'
    @Bean
    Queue responseGetAllQueue() {
        return new Queue(queueResponseGetAll, false);
    }

    @Bean
    TopicExchange getAllExchange() {
        return new TopicExchange(topicExchangResponseGetAll);
    }

    // Define la cola 'find.user.by.id' y el intercambio 'find.user.by.id'
    @Bean
    public Queue findUserByIdQueue() {
        return new Queue(queueFindUserById, false);
    }

    @Bean
    public TopicExchange findUserByIdExchange() {
        return new TopicExchange(topicExchangeFindUserById);
    }

    // Define la cola 'user-id-response' y el intercambio 'user-exchange-response'
    @Bean
    public Queue userIdResponseQueue() {
        return new Queue(queueUserIdResponse, false);
    }

    @Bean
    public TopicExchange userResponseExchange() {
        return new TopicExchange(topicExchangeUserResponse);
    }

    // Define el enlace entre la cola 'get.all.users' y el intercambio
    // 'user-exchange'
    @Bean
    Binding getAllUsersBinding(Queue getAllUsersQueue, TopicExchange getAllUsersExchange) {
        return BindingBuilder.bind(getAllUsersQueue).to(getAllUsersExchange).with("foo.bar.baz");
    }

    // Define el enlace entre la cola 'response.get.all.users' y el intercambio
    // 'response-get-all-users'
    @Bean
    Binding responseBinding(@Qualifier("responseGetAllQueue") Queue queueResponseGetAll, TopicExchange getAllExchange) {
        return BindingBuilder.bind(queueResponseGetAll).to(getAllExchange).with("foo.bar.baz");
    }

    // Define el enlace entre la cola 'find.user.by.id' y el intercambio
    // 'find-user-by-id'
    @Bean
    public Binding bindingFindUserByIdQueue(@Qualifier("findUserByIdQueue") Queue queue,
            @Qualifier("findUserByIdExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("find-user-by-id");
    }

    // Define el enlace entre la cola 'user-id-response' y el intercambio
    // 'user-exchange-response'
    @Bean
    public Binding bindingUserIdResponseQueue(@Qualifier("userIdResponseQueue") Queue queue,
            @Qualifier("userResponseExchange") TopicExchange userResponseExchange) {
        return BindingBuilder.bind(queue).to(userResponseExchange).with("user-id-response");
    }

}
