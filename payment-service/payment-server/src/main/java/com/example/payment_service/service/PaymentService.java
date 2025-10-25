package com.example.payment_service.service;

import com.example.payment_client.dto.PaymentRequest;
import com.example.payment_client.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    PaymentResponse getPaymentByOrderId(String orderId);
}
