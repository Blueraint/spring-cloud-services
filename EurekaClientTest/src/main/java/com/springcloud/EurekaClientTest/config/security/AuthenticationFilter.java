package com.springcloud.EurekaClientTest.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springcloud.EurekaClientTest.domain.Login;
import com.springcloud.EurekaClientTest.dto.UserDto;
import com.springcloud.EurekaClientTest.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;

    private final Environment env;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            Login login = new ObjectMapper().readValue(request.getInputStream(), Login.class);

            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPasswd(), new ArrayList<>()));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Exception while Login Filter");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("Login Info : {}", (authResult.getPrincipal()).toString());

        String username = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = userService.getUserByEmail(username);

        // Build jwt
        String token = Jwts.builder()
                .setSubject(userDto.getUserId())
                        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                .compact();

        log.info("Login Successful");

        response.addHeader("token", token);
        response.addHeader("userId", userDto.getUserId());
        // Call index page("/")
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
