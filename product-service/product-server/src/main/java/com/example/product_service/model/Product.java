package com.example.product_service.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products") // collection trong Mongo
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;

    @Indexed(unique = true)
    private String skuCode;

    private String name;
    private String description;
    private double price;
    private int stock;
}
