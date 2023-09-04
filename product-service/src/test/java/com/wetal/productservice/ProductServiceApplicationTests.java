package com.wetal.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetal.productservice.dto.ProductCreate;
import com.wetal.productservice.model.Product;
import com.wetal.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
   @Container
   static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

   @Autowired
   private MockMvc mockMvc;
   @Autowired
   private ObjectMapper objectMapper;
   @Autowired
   private ProductRepository productRepository;

   @DynamicPropertySource
   static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
      dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
   }

   @Test
   void newProduct() throws Exception {
      ProductCreate productCreate = getProductCreate();
      String stringProductCreate = objectMapper.writeValueAsString(productCreate);

      mockMvc.perform(MockMvcRequestBuilders.post("/api/product/new")
         .contentType(MediaType.APPLICATION_JSON)
         .content(stringProductCreate)
      ).andExpect(status().isCreated());

      Assertions.assertEquals(1, productRepository.findAll().size());

      Product product = productRepository.findAll().get(0);
      Assertions.assertNotNull(product.getId());
      Assertions.assertEquals("iPhone 10", product.getName());
      Assertions.assertEquals("iPhone 10 description", product.getDescription());
      Assertions.assertEquals(BigDecimal.valueOf(1200), product.getPrice());
   }

   @Test
   void allProduct() throws Exception {
      ProductCreate productCreate = ProductCreate.builder()
         .name("iPhone 11")
         .description("iPhone 11 description")
         .price(BigDecimal.valueOf(1300))
         .build();
      String stringProductCreate2 = objectMapper.writeValueAsString(productCreate);

      mockMvc.perform(MockMvcRequestBuilders.post("/api/product/new")
         .contentType(MediaType.APPLICATION_JSON)
         .content(stringProductCreate2)
      ).andExpect(status().isCreated());

      mockMvc.perform(MockMvcRequestBuilders.get("/api/product/all"))
         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         .andExpect(status().isOk())
         .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("iPhone 10"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value("iPhone 10 description"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value(BigDecimal.valueOf(1200)))
         .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").exists())
         .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("iPhone 11"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.[1].description").value("iPhone 11 description"))
         .andExpect(MockMvcResultMatchers.jsonPath("$.[1].price").value(BigDecimal.valueOf(1300)));
   }

   private ProductCreate getProductCreate() {
      return ProductCreate.builder()
         .name("iPhone 10")
         .description("iPhone 10 description")
         .price(BigDecimal.valueOf(1200))
         .build();
   }

}


