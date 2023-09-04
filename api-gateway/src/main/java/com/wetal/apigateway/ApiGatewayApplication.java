package com.wetal.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
   public static void main(String[] args) {
      SpringApplication.run(ApiGatewayApplication.class, args);
   }
   // https://www.youtube.com/watch?v=kn5h-GTjDY0&list=PLSVW22jAG8pBnhAdq9S8BpLnZ0_jVBj0c&index=6
}
