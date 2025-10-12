package com.example.product_client.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private String id;
    private String skuCode;
    private String name;
    private String description;
    private double price;
    private int stock;
}
