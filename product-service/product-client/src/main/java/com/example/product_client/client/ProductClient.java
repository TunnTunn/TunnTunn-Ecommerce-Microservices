package com.example.product_client.client;

import com.example.product_client.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    Optional<ProductResponse> getProductById(@PathVariable("id") String id);

    @PostMapping("/api/products/batch")
    List<ProductResponse> getProductsByIds(@RequestBody List<String> productIds);
}