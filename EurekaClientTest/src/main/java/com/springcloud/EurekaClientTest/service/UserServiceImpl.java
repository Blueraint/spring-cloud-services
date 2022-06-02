package com.springcloud.EurekaClientTest.service;

import com.springcloud.EurekaClientTest.domain.User;
import com.springcloud.EurekaClientTest.dto.UserDto;
import com.springcloud.EurekaClientTest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

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
