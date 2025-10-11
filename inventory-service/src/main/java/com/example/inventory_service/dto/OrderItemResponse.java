package com.example.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal; // Thêm import

@Data
public class OrderItemResponse {
    private Long id; // ⭐️ Thêm trường này
    private String productName;
    private Integer quantity;
    private BigDecimal price; // ⭐️ Thêm trường này
}