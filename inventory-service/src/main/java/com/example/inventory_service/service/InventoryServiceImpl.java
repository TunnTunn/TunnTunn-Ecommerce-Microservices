package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.InventoryResponse;
import com.example.inventory_service.model.Inventory;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryResponse upsertInventory(InventoryRequest request) {
        Inventory inventory = inventoryRepository.findBySkuCode(request.getSkuCode())
                .map(existing -> {
                    existing.setQuantity(request.getQuantity());
                    return existing;
                })
                .orElse(Inventory.builder()
                        .skuCode(request.getSkuCode())
                        .quantity(request.getQuantity())
                        .build());

        Inventory saved = inventoryRepository.save(inventory);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventories() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryBySku(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode)
                .orElseThrow(() -> new IllegalArgumentException("SKU not found: " + skuCode));
        return mapToResponse(inventory);
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }

    private InventoryResponse mapToResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .skuCode(inventory.getSkuCode())
                .quantity(inventory.getQuantity())
                .build();
    }
}
