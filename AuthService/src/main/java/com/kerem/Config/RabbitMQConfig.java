package com.kerem.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // 1.adım: Queue'lerin tanımlanması
    @Bean
    Queue queueA() {
        return new Queue("q.A");
    }

    @Bean
    Queue queueB() {
        return new Queue("q.B");
    }

    @Bean
    Queue queueC() {
        return new Queue("q.C");
    }

    @Bean
    Queue forgotPasswordQueue(){
        return new Queue("forgotPassword.Queue");
    }

    // 2.adım: Exchange oluşturulması
    @Bean
    DirectExchange exchange() {
        return new DirectExchange("exchange.direct");
    }

    // 3.adım: Queue'lerin Exchange'e bağlanması (Binding)
    @Bean
    Binding binding(Queue queueA, DirectExchange exchange) {
        return BindingBuilder
                .bind(queueA)
                .to(exchange)
                .with("routing.A");
    }

    @Bean
    Binding bindingB(Queue queueB, DirectExchange exchange) {
        return BindingBuilder
                .bind(queueB)
                .to(exchange)
                .with("routing.B");
    }

    @Bean
    Binding bindingC(Queue queueC, DirectExchange exchange) {
        return BindingBuilder
                .bind(queueC)
                .to(exchange)
                .with("routing.C");
    }

    @Bean
    Binding bindingD(Queue forgotPasswordQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(forgotPasswordQueue)
                .to(exchange)
                .with("forgotPasswordRoute");
    }

    // 4.adım: Nesne - JSON dönüşümü için Converter'ın tanımlanması
    @Bean
    MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // 5.adım: Converter'ı RabbitTemplate'e tanımlamak
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
