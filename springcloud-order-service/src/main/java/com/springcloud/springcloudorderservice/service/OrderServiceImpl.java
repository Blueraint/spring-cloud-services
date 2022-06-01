package com.springcloud.springcloudorderservice.service;

import com.springcloud.springcloudorderservice.domain.Order;
import com.springcloud.springcloudorderservice.dto.OrderDto;
import com.springcloud.springcloudorderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());

        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Order order = modelMapper.map(orderDto, Order.class);

        orderRepository.save(order);

        return orderDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return new ModelMapper().map(order, OrderDto.class);
    }

    @Override
    public List<OrderDto> getOrdersByUserId(String userId) {
        List<OrderDto>  orderDtoList = new ArrayList<>();

        orderRepository.findByUserId(userId).forEach(order -> {
            orderDtoList.add(new ModelMapper().map(order, OrderDto.class));
        });

        return orderDtoList;
    }
}
