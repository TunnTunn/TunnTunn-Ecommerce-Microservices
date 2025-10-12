package com.example.cart_service.service;

import com.example.cart_client.dto.AddItemRequest;
import com.example.cart_client.dto.CartItemResponse;
import com.example.cart_client.dto.CartResponse;
import com.example.cart_service.client.ProductClient;
import com.example.cart_service.model.Cart;
import com.example.cart_service.model.CartItem;
import com.example.product_client.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductClient productClient;
    private static final String CART_KEY_PREFIX = "cart:";

    @Override
    public CartResponse getCartByUserId(String userId) {
        Cart cart = findCartByUserId(userId);
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse addItemToCart(String userId, AddItemRequest request) {
        ProductResponse product = productClient.getProductById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + request.getProductId()));

        Cart cart = findCartByUserId(userId);

        CartItem existingItem = cart.getItems().get(request.getProductId());

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .productId(product.getId())
                    .productName(product.getName())
                    .quantity(request.getQuantity())
                    .price(BigDecimal.valueOf(product.getPrice()))
                    .build();
            cart.getItems().put(product.getId(), newItem);
        }

        saveCart(cart);
        return mapToCartResponse(cart);
    }

    @Override
    public void clearCart(String userId) {
        redisTemplate.delete(CART_KEY_PREFIX + userId);
    }

    private Cart findCartByUserId(String userId) {
        Cart cart = (Cart) redisTemplate.opsForValue().get(CART_KEY_PREFIX + userId);
        if (cart == null) {
            return Cart.builder().userId(userId).build();
        }
        return cart;
    }

    private void saveCart(Cart cart) {
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cart.getUserId(), cart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().values().stream()
                .map(item -> CartItemResponse.builder()
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());

        BigDecimal totalPrice = itemResponses.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Chú ý: không có cartId vì chúng ta không lưu nó vào DB quan hệ
        return CartResponse.builder()
                .userId(cart.getUserId())
                .items(itemResponses)
                .totalPrice(totalPrice)
                .build();
    }
}