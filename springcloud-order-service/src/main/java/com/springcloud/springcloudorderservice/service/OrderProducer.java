package com.springcloud.springcloudorderservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.springcloudorderservice.domain.Field;
import com.springcloud.springcloudorderservice.domain.Payload;
import com.springcloud.springcloudorderservice.domain.Schema;
import com.springcloud.springcloudorderservice.dto.KafkaOrderDto;
import com.springcloud.springcloudorderservice.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    List<Field> fields = Arrays.asList(
            new Field("string", true, "order_id"),
            new Field("string", true, "user_id"),
            new Field("string", true, "product_id"),
            new Field("int32", true, "qty"),
            new Field("int32", true, "unit_price"),
            new Field("int32", true, "total_price")
    );

    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("orders")
            .build();

    public KafkaOrderDto send(String kafkaTopic, OrderDto orderDto) {
        Payload payload = Payload.builder()
                        .order_id(orderDto.getOrderId())
                                .user_id(orderDto.getUserId())
                                        .product_id(orderDto.getProductId())
                                                .qty(orderDto.getQty())
                                                        .unit_price(orderDto.getUnitPrice())
                                                                .total_price(orderDto.getTotalPrice())
                .build();

        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        String jsonInString = "";

        try {
            jsonInString = objectMapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        kafkaTemplate.send(kafkaTopic, jsonInString);
        log.info("Order Producer sening data from the Order Microservice : " + kafkaOrderDto.toString());

        return kafkaOrderDto;
    }

}
