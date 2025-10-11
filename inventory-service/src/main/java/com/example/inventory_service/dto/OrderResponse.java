package com.example.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal; // Thêm import
import java.time.LocalDateTime; // Thêm import
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String customerName; // ⭐️ Thêm trường này
    private LocalDateTime createdAt; // ⭐️ Thêm trường này
    private BigDecimal totalAmount; // ⭐️ Thêm trường này
    // Bạn không cần trường status vì nó là enum, nhưng nếu có thì cũng không sao
    private List<OrderItemResponse> items;
}