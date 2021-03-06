package com.springcloud.EurekaClientTest.client;

import com.springcloud.EurekaClientTest.domain.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * interface from API Call(FeignClient)
 */
@FeignClient(name = "order-service")
public interface OrderServiceClient {

    //    @GetMapping("/order-service/{userId}/orders_err")
    @GetMapping("/order-service/{userId}/orders")
    List<Order> getOrders(@PathVariable("userId") String userId);
}
