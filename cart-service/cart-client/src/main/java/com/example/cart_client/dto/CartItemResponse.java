package com.example.cart_client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}