package com.wetal.productservice.controller;

import com.wetal.productservice.dto.ProductCreate;
import com.wetal.productservice.dto.ProductResponse;
import com.wetal.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

   private final ProductService productService;

   @PostMapping("/new")
   @ResponseStatus(HttpStatus.CREATED)
   @ResponseBody
   public void create(@RequestBody ProductCreate productCreate) {
      productService.create(productCreate);
   }

   @GetMapping("/all")
   public List<ProductResponse> getAll() {
      return productService.getAllProductResponse();
   }
}
