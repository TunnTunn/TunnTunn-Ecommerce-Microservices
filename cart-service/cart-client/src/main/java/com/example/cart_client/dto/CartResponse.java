package com.example.cart_client.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long id;
    private String userId;
    private List<CartItemResponse> items;
    private BigDecimal totalPrice;
}