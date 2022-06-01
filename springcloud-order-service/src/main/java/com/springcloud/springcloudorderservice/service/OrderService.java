package com.springcloud.springcloudorderservice.service;

import com.springcloud.springcloudorderservice.domain.Order;
import com.springcloud.springcloudorderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    List<OrderDto> getOrdersByUserId(String userId);
}
