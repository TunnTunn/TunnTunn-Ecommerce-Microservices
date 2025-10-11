package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.OrderResponse; // Bạn cần tạo DTO này
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMessageListener {

    private final InventoryService inventoryService;

    // DTO để khớp với dữ liệu từ order-service
    // Bạn cần tạo file này trong inventory-service
    // Ví dụ: com.example.inventory_service.dto.OrderResponse
    // với các field cần thiết như List<OrderItemResponse> items;

    @RabbitListener(queues = "inventory_queue")
    public void receiveMessage(OrderResponse order) {
        System.out.println("Received message for Order ID: " + order.getId());

        order.getItems().forEach(item -> {
            // Lấy SkuCode (ở đây giả định productName là skuCode) và số lượng
            String skuCode = item.getProductName();
            Integer quantityToReduce = item.getQuantity();

            // Lấy thông tin inventory hiện tại
            var currentInventory = inventoryService.getInventoryBySku(skuCode);

            // Tạo request để cập nhật
            InventoryRequest updateRequest = new InventoryRequest();
            updateRequest.setSkuCode(skuCode);
            updateRequest.setQuantity(currentInventory.getQuantity() - quantityToReduce);

            // Cập nhật inventory
            inventoryService.upsertInventory(updateRequest);

            System.out.println("Updated inventory for SKU: " + skuCode);
        });
    }
}