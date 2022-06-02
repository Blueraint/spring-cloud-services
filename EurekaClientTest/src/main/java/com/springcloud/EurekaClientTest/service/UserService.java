package com.springcloud.EurekaClientTest.service;

import com.springcloud.EurekaClientTest.dto.UserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    List getAllUser();
    UserDto getUserByEmail(String username);
}
