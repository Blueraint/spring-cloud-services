package com.springcloud.springcloudcatalogservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.springcloudcatalogservice.domain.Catalog;
import com.springcloud.springcloudcatalogservice.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final CatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "example-order-topic")
    public void processMsg(String kafkaMessage) {
        log.info("Kafka Msg : {}", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();

        try {
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Catalog catalog = catalogRepository.findByProductId((String)map.get("productId")).orElseGet(null);
        catalog.setStock(catalog.getStock() - (Integer)map.get("qty"));

        catalogRepository.save(catalog);
    }
}
