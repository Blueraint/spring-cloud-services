package com.spring.springclouduserservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringcloudUserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudUserServiceApplication.class, args);
	}

}
