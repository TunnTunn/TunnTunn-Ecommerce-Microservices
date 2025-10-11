package com.example.inventory_service.service;

import com.example.inventory_service.dto.InventoryRequest;
import com.example.inventory_service.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryResponse upsertInventory(InventoryRequest request);
    List<InventoryResponse> getAllInventories();
    InventoryResponse getInventoryBySku(String skuCode);
    void deleteInventory(Long id);
}
