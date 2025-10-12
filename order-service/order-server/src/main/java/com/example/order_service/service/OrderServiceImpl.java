package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.event.OrderEventPublisher;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_events.dto.OrderStatus;
import com.example.order_service.model.Order;
import com.example.order_service.model.OrderItem;
import com.example.order_service.repository.OrderRepository;
import com.example.product_client.dto.ProductResponse;
import com.example.order_client.dto.OrderRequest;
import com.example.order_events.dto.OrderResponse;
import com.example.order_client.dto.OrderItemRequest;
import com.example.order_events.dto.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    public OrderResponse createOrder(OrderRequest request) {
        // Fetch products from Product Service
        List<ProductResponse> products = fetchProductsByIds(request);

        // Validate & build OrderItems
        List<OrderItem> orderItems = buildOrderItems(request, products);

        // Calculate total & save order
        Order savedOrder = saveOrder(request, orderItems);

        // Convert to response DTO
        OrderResponse orderResponse = orderMapper.toOrderResponse(savedOrder);

        // Send order created message
        orderEventPublisher.publishOrderCreatedEvent(orderResponse);

        return orderResponse;
    }

    // Fetch products from Product Service
    private List<ProductResponse> fetchProductsByIds(OrderRequest request) {
        List<String> productIds = request.getItems().stream()
                .map(OrderItemRequest::getProductId)
                .toList();

        // Thay thế bằng một dòng gọi duy nhất, sạch sẽ và dễ hiểu
        List<ProductResponse> products = productClient.getProductsByIds(productIds);

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("No products found for provided IDs");
        }

        return products;
    }

    // Validate & build OrderItems
    private List<OrderItem> buildOrderItems(OrderRequest request, List<ProductResponse> products) {
        Map<String, ProductResponse> productMap = products.stream()
                .collect(Collectors.toMap(ProductResponse::getId, p -> p));

        return request.getItems().stream()
                .map(item -> {
                    ProductResponse product = productMap.get(item.getProductId());
                    if (product == null)
                        throw new IllegalArgumentException("Product not found: " + item.getProductId());
                    if (product.getStock() < item.getQuantity())
                        throw new IllegalArgumentException("Not enough stock for: " + product.getName());

                    return OrderItem.builder()
                            .productId(product.getId())
                            .skuCode(product.getSkuCode())
                            .productName(product.getName())
                            .quantity(item.getQuantity())
                            .price(BigDecimal.valueOf(product.getPrice()))
                            .build();
                })
                .toList();
    }

    // Calculate total & save order
    private Order saveOrder(OrderRequest request, List<OrderItem> orderItems) {
        BigDecimal total = orderItems.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .totalAmount(total)
                .items(orderItems)
                .build();

        orderItems.forEach(i -> i.setOrder(order));
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toOrderResponses(orderRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
