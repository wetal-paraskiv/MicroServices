package com.wetal.inventoryservice.service;

import com.wetal.inventoryservice.dto.InventoryResponse;
import com.wetal.inventoryservice.model.Inventory;
import com.wetal.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryService {
   private final InventoryRepository inventoryRepository;

   @Transactional(readOnly = true)
   public List<InventoryResponse> isInStock(List<String> skuCodesList) throws InterruptedException {
      log.info("...Checking Inventory...");
//      log.info("...Pausing inventory service for 5 seconds...");
//      Thread.sleep(10000);
//      log.info("...Resuming inventory service...");
      return inventoryRepository.findBySkuCodeIn(skuCodesList).stream()
         .map(inventory ->
            InventoryResponse.builder()
               .skuCode(inventory.getSkuCode())
               .quantity(inventory.getQuantity())
               .build()
         ).toList();
   }
}
