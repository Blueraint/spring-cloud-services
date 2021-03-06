package com.spring.SpringCloudGatewayTest.filter;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
    private Environment env;

    public AuthorizationHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {
        // Put configuration properties
    }

    // Get token and authorize token
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            // Reactive type
            ServerHttpRequest req = exchange.getRequest();

            if(!req.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = req.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if(!isJwtValid(jwt)) {
                return onError(exchange, "Jwt token is not Valid", HttpStatus.UNAUTHORIZED);
            }


            return chain.filter(exchange)
                    ;
        });
    }

    private boolean isJwtValid(String token) {
        boolean flag = true;
        String subject = new String();

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token).getBody()
                    .getSubject();
        } catch (Exception e) {
            flag = false;
            log.error(e.getMessage());
        }

        if(subject == null || subject.isEmpty()) {
            flag = false;
        }

        log.info("user-service subject : {}", subject);

        return flag;
    }

    // err??? ???????????? mono??? ????????? complete??? ??????
    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse res = exchange.getResponse();
        res.setStatusCode(httpStatus);

        log.error(err);
        return res.setComplete();
    }
}
