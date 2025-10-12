package com.example.cart_service.client;

import com.example.product_client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "product-service")
public interface ProductClient {

    // Endpoint này phải khớp với endpoint trong ProductController
    @GetMapping("/api/products/{id}")
    Optional<ProductResponse> getProductById(@PathVariable("id") String id);
}