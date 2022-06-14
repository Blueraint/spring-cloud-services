package com.springcloud.springcloudorderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.springcloudorderservice.domain.Order;
import com.springcloud.springcloudorderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper;

    public OrderDto send(String kafkaTopic, OrderDto orderDto) {
        String jsonInString = "";

        try {
            jsonInString = objectMapper.writeValueAsString(orderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Kafka Producer sening data from the Order Microservice : " + orderDto.toString());

        return orderDto;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());

        orderDto.setTotalPrice(orderDto.getQty() * orderDto.getUnitPrice());

        orderProducer.send("orders", orderDto);

        return orderDto;
    }
}
