package com.example.inventory_service.config;

import com.example.order_events.OrderEventConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue queue() {
        return new Queue(OrderEventConstants.ORDER_CREATED_QUEUE, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(OrderEventConstants.ORDER_EXCHANGE);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(OrderEventConstants.ORDER_CREATED_ROUTING_KEY);
    }

    // Converter to serialize and deserialize messages to/from JSON
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}