package com.example.order_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.example.order_service.config.RabbitMQConfig;
import com.example.order_service.dto.OrderResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public void sendOrderCreatedMessage(OrderResponse order) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, order);
        System.out.println("Sent message for Order ID: " + order.getId());
    }
}