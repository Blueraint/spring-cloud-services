package config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                // define first-service api
                .route(r -> r.path("/first-service/**") // request 의 출발 지점
                            .filters(f -> f.addRequestHeader("first-request","first-request-header-value")
                                    .addResponseHeader("first-response","first-response-header-value")) // custom filter 를 지나면서 정해진 business logic 수행
                            .uri("http://localhost:8081")) // request 가 요청하는 자원의 목적지점(destination)
                // define second-service api
                .route(r -> r.path("/second-service/**") // request 의 출발 지점
                        .filters(f -> f.addRequestHeader("second-request","second-request-header-value")
                                .addResponseHeader("second-response","second-response-header-value")) // custom filter 를 지나면서 정해진 business logic 수행
                        .uri("http://localhost:8085")) // request 가 요청하는 자원의 목적지점(destination)
                .build();

    }
}
