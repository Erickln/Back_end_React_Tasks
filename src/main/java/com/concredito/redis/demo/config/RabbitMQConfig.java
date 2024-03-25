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

    public static final String topicExchangeUser = "user-exchange";

    public static final String queueUser = "user-queue";

    public static final String topicExchangeGetAll = "get-all-exchange";

    public static final String queueGetAll = "get-all-queue";

    public static final String topicExchangResponseGetAll = "response-get-all-users";

    public static final String queueResponseGetAll = "response-get-all-users-queue";

    @Bean
    Queue queue() {
        return new Queue(queueUser, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeUser);
    }

    @Bean
    Queue getAllUsersQueue() {
        return new Queue(queueGetAll, false);
    }

    // Define el intercambio para la cola 'get.all.users'
    @Bean
    TopicExchange getAllUsersExchange() {
        return new TopicExchange(topicExchangeGetAll);
    }

    @Bean
    Queue responseGetAllQueue() {
        return new Queue(queueResponseGetAll, false);
    }

    @Bean
    TopicExchange getAllExchange() {
        return new TopicExchange(topicExchangResponseGetAll);
    }

    // Define el enlace entre la cola 'get.all.users' y el intercambio
    // 'user-exchange'
    @Bean
    Binding getAllUsersBinding(Queue getAllUsersQueue, TopicExchange getAllUsersExchange) {
        return BindingBuilder.bind(getAllUsersQueue).to(getAllUsersExchange).with("foo.bar.baz");
    }

    // Define un enlace genérico para la cola principal y el intercambio principal
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    Binding responseBinding(@Qualifier("responseGetAllQueue") Queue queueResponseGetAll, TopicExchange getAllExchange) {
        return BindingBuilder.bind(queueResponseGetAll).to(getAllExchange).with("foo.bar.baz");
    }

}
