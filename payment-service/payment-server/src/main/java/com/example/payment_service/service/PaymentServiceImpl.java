package com.example.payment_service.service;

import com.example.payment_client.dto.PaymentRequest;
import com.example.payment_client.dto.PaymentResponse;
import com.example.payment_client.dto.PaymentStatus;
import com.example.payment_service.model.Payment;
import com.example.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // 🔹 Giả lập xử lý thanh toán — sau này có thể thay bằng call tới payment
        // gateway
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .status(PaymentStatus.SUCCESS)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapToResponse(payment);
    }

    // 🔸 Helper method
    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .build();
    }
}
