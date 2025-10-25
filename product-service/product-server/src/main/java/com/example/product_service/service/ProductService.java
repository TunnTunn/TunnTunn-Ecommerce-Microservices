package com.example.product_service.service;

import com.example.product_client.dto.ProductRequest;
import com.example.product_client.dto.ProductResponse;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Optional<ProductResponse> getProductById(String id) {
        return productRepository.findById(id).map(this::mapToResponse);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = Product.builder()
                .skuCode(request.getSkuCode())
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setSkuCode(request.getSkuCode());
                    existing.setName(request.getName());
                    existing.setDescription(request.getDescription());
                    existing.setPrice(request.getPrice());
                    existing.setStock(request.getStock());
                    return mapToResponse(productRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<ProductResponse> getProductsByIds(List<String> ids) {
        return productRepository.findByIdIn(ids)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .skuCode(product.getSkuCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
