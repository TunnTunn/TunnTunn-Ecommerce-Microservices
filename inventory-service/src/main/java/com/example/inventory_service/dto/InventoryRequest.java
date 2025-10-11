package com.example.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequest {

    @NotBlank(message = "SKU code must not be blank")
    private String skuCode;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}
