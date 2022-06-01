package com.spring.SpringCloudGatewayTest.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public GlobalFilter() {
        super(Config.class);
    }

    @Data
    public static class Config {
        // Put configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // Reactive type
            ServerHttpRequest req = exchange.getRequest();
            ServerHttpResponse res = exchange.getResponse();

            log.info("Global filter : baseMessage -> {}", config.baseMessage);

            if(config.isPreLogger()) {
                log.info("Global PRE filter : request uri -> {}", req.getId());
            }

            return chain.filter(exchange)
                    // reactive mono
                    .then(Mono.fromRunnable(() -> {
                        if(config.isPostLogger()) {
                            log.info("Global POST filter : response code -> {}", res.getStatusCode());
                        }
                    }));
        });
    }
}
