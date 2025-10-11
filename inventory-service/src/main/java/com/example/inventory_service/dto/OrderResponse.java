package com.example.inventory_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> items;
}