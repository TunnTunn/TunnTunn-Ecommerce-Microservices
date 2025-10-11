package com.example.inventory_service.event;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.OrderResponse;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
// Đổi tên class
public class OrderEventListener {

    private final InventoryService inventoryService;

    // Listen to messages from the inventory_queue
    @RabbitListener(queues = "inventory_queue")
    public void handleOrderCreatedEvent(OrderResponse order) {
        log.info("Received Order Created Event for Order ID: {}", order.getId());

        order.getItems().forEach(item -> {
            try {
                String skuCode = item.getProductName(); // Giả định productName là skuCode
                Integer quantityToReduce = item.getQuantity();

                var currentInventory = inventoryService.getInventoryBySku(skuCode);
                int newQuantity = currentInventory.getQuantity() - quantityToReduce;

                if (newQuantity < 0) {
                    log.error("Inventory for SKU {} would go negative. Halting update.", skuCode);
                    // Có thể gửi một event khác để xử lý việc này (vd: cancel order)
                    return;
                }

                InventoryRequest updateRequest = new InventoryRequest();
                updateRequest.setSkuCode(skuCode);
                updateRequest.setQuantity(newQuantity);

                inventoryService.upsertInventory(updateRequest);
                log.info("Successfully updated inventory for SKU: {}. New quantity: {}", skuCode, newQuantity);

            } catch (Exception e) {
                log.error("Error processing inventory update for item {}: {}", item.getProductName(), e.getMessage());
                // Xử lý lỗi, có thể đưa vào một Dead Letter Queue
            }
        });
    }
}