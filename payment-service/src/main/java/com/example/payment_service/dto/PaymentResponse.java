package com.example.payment_service.dto;

import com.example.payment_service.model.PaymentStatus;
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
