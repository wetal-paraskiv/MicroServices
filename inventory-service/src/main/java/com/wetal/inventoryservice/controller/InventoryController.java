package com.wetal.inventoryservice.controller;

import com.wetal.inventoryservice.dto.InventoryResponse;
import com.wetal.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

   private final InventoryService inventoryService;

   @GetMapping
   @ResponseStatus(HttpStatus.OK)
   public List<InventoryResponse> isInStock(@RequestParam List<String> skuCodesList) throws InterruptedException {
      return inventoryService.isInStock(skuCodesList);
   }
}
