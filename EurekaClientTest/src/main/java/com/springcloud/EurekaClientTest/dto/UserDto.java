package com.springcloud.EurekaClientTest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.springcloud.EurekaClientTest.domain.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String email;

    private String name;

    private String passwd;

    private String userId;

    private Date createdAt;

    private String encryptedPwd;

    private List<Order> orderList;
}
