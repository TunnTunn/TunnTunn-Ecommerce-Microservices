package com.example.payment_service.controller;

import com.example.payment_client.dto.PaymentRequest;
import com.example.payment_client.dto.PaymentResponse;
import com.example.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request);
    }

    @GetMapping("/{orderId}")
    public PaymentResponse getPaymentByOrder(@PathVariable String orderId) {
        return paymentService.getPaymentByOrderId(orderId);
    }
}
