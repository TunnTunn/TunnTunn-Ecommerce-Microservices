package com.example.order_service.event;

import com.example.order_service.config.RabbitMQConfig;
import com.example.order_client.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderCreatedEvent(OrderResponse orderResponse) {
        log.info("Publishing Order Created Event for Order ID: {}", orderResponse.getId());
        // Send the orderResponse object directly
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                orderResponse);
    }
}