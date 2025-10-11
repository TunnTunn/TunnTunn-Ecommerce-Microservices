package com.example.payment_client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String orderId;
    private Double amount;
    private PaymentStatus status;
}
