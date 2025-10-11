package com.example.payment_client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank
    private String orderId;

    @NotNull
    private Double amount;
}
