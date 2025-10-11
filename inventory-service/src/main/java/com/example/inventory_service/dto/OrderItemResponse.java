package com.example.inventory_service.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String productName; // Sẽ được dùng như skuCode
    private Integer quantity;
}