package com.example.order_service.dto;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private double price;
    private int stock;
}