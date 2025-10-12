package com.example.cart_service.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class CartItem implements Serializable {
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}