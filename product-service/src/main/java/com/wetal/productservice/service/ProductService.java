package com.wetal.productservice.service;

import com.wetal.productservice.dto.ProductCreate;
import com.wetal.productservice.dto.ProductResponse;
import com.wetal.productservice.model.Product;
import com.wetal.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


//@RequiredArgsConstructor // exclude constructor
@Service
@Slf4j
public class ProductService {

   private final ProductRepository productRepository;

   @Autowired
   public ProductService(ProductRepository productRepository) {
      this.productRepository = productRepository;
   }


   public void create(ProductCreate productCreate) {
      Product product = Product.builder()
         .name(productCreate.getName())
         .description(productCreate.getDescription())
         .price(productCreate.getPrice())
         .build();

      productRepository.save(product);

      log.info("Created Product with id: {}", product.getId());
   }

   public List<ProductResponse> getAllProductResponse() {
      List<Product> products = productRepository.findAll();
      log.info("Fetched all products from repository...");
      return products.stream().map(this::mapToProductResponse).toList();
   }

   private ProductResponse mapToProductResponse(Product product) {
      return ProductResponse.builder()
         .id(product.getId())
         .name(product.getName())
         .description(product.getDescription())
         .price(product.getPrice())
         .build();
   }
}
