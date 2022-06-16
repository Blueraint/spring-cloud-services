package com.springcloud.springcloudorderservice.controller;

import com.springcloud.springcloudorderservice.dto.OrderDto;
import com.springcloud.springcloudorderservice.service.KafkaProducerService;
import com.springcloud.springcloudorderservice.service.OrderService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {
    private final Environment env;
    private final OrderService orderService;
    private final KafkaProducerService kafkaProducerService;

    @Value("${messages.health-check}")
    private String healthCheckMessage;

    @GetMapping("/health-check")
    @Timed(value = "orders.status", longTask = true)
    public String status() {
        return String.format("[msg] %s  [port] %s", healthCheckMessage, env.getProperty("local.server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity createOrder(@PathVariable("userId") String userId, @RequestBody OrderDto orderDto) {
        log.info("Before Add Orders MicroService Data.");

        orderDto.setUserId(userId);

        // use Jpa
        OrderDto responseOrderDto = orderService.createOrder(orderDto);

        // use Kafka
//        OrderDto responseOrderDto = kafkaProducerService.createOrder(orderDto);

        // send orders to kafka topic
//        kafkaProducerService.send("example-order-topic", orderDto);

        log.info("After Add Orders MicroService Data.");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrderDto);
    }

    @GetMapping("{userId}/orders")
    public ResponseEntity<List> getOrdersByUserId(@PathVariable("userId") String userId) {
        List<OrderDto> orderDtoList = orderService.getOrdersByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(orderDtoList);
    }

    @GetMapping("{orderId}/order")
    public ResponseEntity<OrderDto> getOrdersByOrderId(@PathVariable("orderId") String orderId) throws Exception {
        log.info("Before Retrieve Orders MicroService Data.");
        OrderDto orderDto = orderService.getOrderByOrderId(orderId);

        // 강제 Error 발생
        /*
        try {
            Thread.sleep(1000);
            throw new Exception("Error Occured.");
        }catch(InterruptedException e) {
            log.error(e.getMessage());
        }
         */
        log.info("After Retrieve Orders MicroService Data.");

        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }
}
