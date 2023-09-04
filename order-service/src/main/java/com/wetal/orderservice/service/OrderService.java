package com.wetal.orderservice.service;

import com.wetal.orderservice.dto.InventoryResponse;
import com.wetal.orderservice.dto.OrderLineItemDto;
import com.wetal.orderservice.dto.OrderRequest;
import com.wetal.orderservice.event.OrderPlacedEvent;
import com.wetal.orderservice.model.Order;
import com.wetal.orderservice.model.OrderLineItem;
import com.wetal.orderservice.repositoty.OrderRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class OrderService {
   private final OrderRepository orderRepository;
   private final WebClient.Builder webClientBuilder;
   private final Tracer tracer;
   private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

   public String placeOrder(OrderRequest orderRequest) {
      Order order = new Order();
      order.setOrderNumber(UUID.randomUUID().toString());

      List<OrderLineItem> orderLineItemList = orderRequest.getOrderLineItemDtoList().stream()
         .map(this::mapToOrderLineItem)
         .toList();

      order.setOrderLineItemsList(orderLineItemList);

      List<String> skuCodesList = order.getOrderLineItemsList().stream()
         .map(OrderLineItem::getSkuCode)
         .toList();

      Span inventoryServiceLookUp = tracer.nextSpan().name("InventoryServiceLookUp");
      try (Tracer.SpanInScope span = tracer.withSpan(inventoryServiceLookUp.start())) {

         InventoryResponse[] inventoryResponse = webClientBuilder.build().get()
            .uri("http://inventory-service/api/inventory",
               uriBuilder -> uriBuilder.queryParam("skuCodesList", skuCodesList).build())
            .retrieve()
            .bodyToMono(InventoryResponse[].class)
            .block();

         if (inventoryResponse.length > 0) {
            List<InventoryResponse> outOfStock = new ArrayList<>();
            for (OrderLineItem oli : order.getOrderLineItemsList()) {
               InventoryResponse iResp = Arrays.stream(inventoryResponse).filter(ir -> ir.getSkuCode().equals(oli.getSkuCode())).collect(Collectors.toList()).get(0);
               if (iResp.getQuantity() < oli.getQuantity()) {
                  outOfStock.add(iResp);
               }
            }
            if (outOfStock.size() == 0) {
               orderRepository.save(order);
               kafkaTemplate.send("notificationTopic",
                  OrderPlacedEvent.builder()
                     .orderNumber(order.getOrderNumber())
                     .build());
               return "Order placed successfully";
            }
            return "Products that are not enough in stock: " + outOfStock;
         }
         return "Unknown products!";
      }
      finally {
         inventoryServiceLookUp.end();
      }
   }

   private OrderLineItem mapToOrderLineItem(OrderLineItemDto orderLineItemDto) {
      return OrderLineItem.builder()
         .skuCode(orderLineItemDto.getSkuCode())
         .quantity(orderLineItemDto.getQuantity())
         .price(orderLineItemDto.getPrice())
         .build();
   }
}
