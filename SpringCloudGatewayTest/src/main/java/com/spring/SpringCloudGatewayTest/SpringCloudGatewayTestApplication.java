package com.spring.SpringCloudGatewayTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringCloudGatewayTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudGatewayTestApplication.class, args);
	}

}
