package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {
    @NotBlank
    private String productId;

    @Min(1)
    private Integer quantity;

    private BigDecimal price;
}
