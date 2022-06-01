package com.spring.SpringCloudGatewayTest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    public CustomFilter() {
        super(Config.class);
    }

    public static class Config {
        // Put configuration properties
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // Reactive type
            ServerHttpRequest req = exchange.getRequest();
            ServerHttpResponse res = exchange.getResponse();

            log.info("Custom PRE filter : request uri -> {}", req.getId());

            return chain.filter(exchange)
                    // reactive mono
                    .then(Mono.fromRunnable(() -> {
                        log.info("Custom POST filter : response code -> {}", res.getStatusCode());
                    }));
        });
    }
}
