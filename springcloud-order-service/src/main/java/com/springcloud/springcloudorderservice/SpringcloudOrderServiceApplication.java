package com.springcloud.springcloudorderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudOrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringcloudOrderServiceApplication.class, args);
	}

}
