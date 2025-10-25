package com.example.cart_service.controller;

import com.example.cart_client.dto.AddItemRequest;
import com.example.cart_client.dto.CartResponse;
import com.example.cart_service.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // TODO: userId sẽ được lấy từ JWT token sau khi tích hợp Keycloak
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponse> addItem(@PathVariable String userId, @Valid @RequestBody AddItemRequest request) {
        return ResponseEntity.ok(cartService.addItemToCart(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}