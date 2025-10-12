package com.example.cart_service.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Cart implements Serializable {
    private String userId;
    // Sử dụng Map<ProductId, CartItem> để truy cập item nhanh hơn
    private Map<String, CartItem> items = new HashMap<>();
}