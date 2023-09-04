package com.wetal.orderservice.controller;

import com.wetal.orderservice.dto.OrderRequest;
import com.wetal.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
   private final OrderService orderService;

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
   @TimeLimiter(name = "inventory")
   public String placeOrder(@RequestBody OrderRequest orderRequest) {
      String responseMessage = orderService.placeOrder(orderRequest);
      return responseMessage;
   }

   public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
      String message = "Oops, something went wrong, you have to repeat order after some time...";
      return message;
   }
}
