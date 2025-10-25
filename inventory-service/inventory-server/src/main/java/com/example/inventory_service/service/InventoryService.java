package com.example.inventory_service.service;

import com.example.inventory_client.dto.InventoryRequest;
import com.example.inventory_client.dto.InventoryResponse;

import java.util.List;

public interface InventoryService {
    InventoryResponse upsertInventory(InventoryRequest request);

    List<InventoryResponse> getAllInventories();

    InventoryResponse getInventoryBySku(String skuCode);

    void deleteInventory(Long id);
}
