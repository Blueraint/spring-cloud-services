package com.springcloud.EurekaClientTest.controller;

import com.springcloud.EurekaClientTest.domain.RequestUser;
import com.springcloud.EurekaClientTest.domain.User;
import com.springcloud.EurekaClientTest.dto.UserDto;
import com.springcloud.EurekaClientTest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
//@RequestMapping("/user-service")
@RequestMapping("/")
public class UserController {
    private final Environment env;
    private final UserService userService;

    @GetMapping("/health-check")
    public String status() {
        return String.format("[msg] %s  [port] %s  [token secret] %s  [token expire time] %s",
                env.getProperty("messages.health-check"), env.getProperty("local.server.port"),
                env.getProperty("token.secret"), env.getProperty("token.expiration_time"));
    }

    @PostMapping("/users")
    public ResponseEntity createUser(@RequestBody RequestUser requestUser) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(requestUser, UserDto.class);
        userService.createUser(userDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable> getUser() {
        List<UserDto> userDtoList = userService.getAllUser();

        return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUserId(userId));
    }
}
