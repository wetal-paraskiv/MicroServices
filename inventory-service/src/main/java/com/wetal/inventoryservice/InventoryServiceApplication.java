package com.wetal.inventoryservice;

import com.wetal.inventoryservice.model.Inventory;
import com.wetal.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

   public static void main(String[] args) {
      SpringApplication.run(InventoryServiceApplication.class, args);
   }

   @Bean
   public CommandLineRunner createData(InventoryRepository inventoryRepository) {
      return args -> {
         Inventory i1 = Inventory.builder()
            .skuCode("iPhone 10")
            .quantity(5)
            .build();
         Inventory i2 = Inventory.builder()
            .skuCode("iPhone 10 Red")
            .quantity(0)
            .build();
         inventoryRepository.save(i1);
         inventoryRepository.save(i2);
      };
   }

}

