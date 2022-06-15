package com.springcloud.EurekaClientTest.service;

import com.springcloud.EurekaClientTest.client.FeignErrorDecoder;
import com.springcloud.EurekaClientTest.client.OrderServiceClient;
import com.springcloud.EurekaClientTest.domain.Order;
import com.springcloud.EurekaClientTest.domain.User;
import com.springcloud.EurekaClientTest.dto.UserDto;
import com.springcloud.EurekaClientTest.repository.UserRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final RestTemplate restTemplate;
    private final Environment env;
    private final OrderServiceClient orderServiceClient;
    private final FeignErrorDecoder feignErrorDecoder;
    private final CircuitBreakerFactory circuitBreakerFactory;


    // by UserDetailService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " is not found"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getEncryptedPwd(),
                true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        User user = modelMapper.map(userDto, User.class);
        user.setEncryptedPwd(encoder.encode(userDto.getPasswd()));

        userRepository.save(user);

        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserDto userDto = new ModelMapper().map(user, UserDto.class);

        // Get Orders From Order MicroService(RestTemplate)

        /*
        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
        ResponseEntity<List<Order>> orderRes =
                restTemplate.exchange(orderUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Order>>() {
                        });

        List<Order> orderList = orderRes.getBody();
        userDto.setOrderList(orderList);
        */

        // Get Orders From Order MicroService(FeignClient Interface)
        /*
        List<Order> orderList = null;
        try {
            orderList = orderServiceClient.getOrders(userId);
        } catch(FeignException e) {
            log.error(e.getMessage());
        }
         */

        // ErrorDecoder에 의해 처리되므로 try-catch 로 Exception catch 하지 않아도 된다
        // CircuitBreaker(resilience4j) 를 이용하여 MicroService 간 connection 등 문제가 생기는 경우 접근을 차단할 수 있도록 한다(ErrorDecoder 이용하지 않아도 됨)
//        List<Order> orderList = orderServiceClient.getOrders(userId);
        log.info("Before Call Orders MicroService.");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        // circuitBreaker 의 동작방식 지정 : method 를 실행하고, 에러가 나는경우 빈 List를 반환한다(Optional과 사용법 유사)
        List<Order> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        log.info("After Call Orders MicroService.");

        userDto.setOrderList(orderList);

        return userDto;
    }

    @Override
    public List getAllUser() {
        List<UserDto> userDtoList = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            userDtoList.add(new ModelMapper().map(user, UserDto.class));
        });

        return userDtoList;
    }

    @Override
    public UserDto getUserByEmail(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found."));

        UserDto userDto = new ModelMapper().map(user, UserDto.class);

        return userDto;
    }
}
