package com.example.cart_service.service;

import com.example.cart_client.dto.AddItemRequest;
import com.example.cart_client.dto.CartResponse;

public interface CartService {
    CartResponse getCartByUserId(String userId);

    CartResponse addItemToCart(String userId, AddItemRequest request);

    void clearCart(String userId);
}