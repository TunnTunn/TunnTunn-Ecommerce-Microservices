package com.example.cart_client.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemRequest {
    @NotBlank
    private String productId;

    @Min(1)
    private Integer quantity;
}