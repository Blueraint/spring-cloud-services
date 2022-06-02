package com.springcloud.EurekaClientTest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class Login {
    @Email
    private String email;

    private String passwd;
}
