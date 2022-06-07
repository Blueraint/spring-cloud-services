package com.spring.SpringCloudConfigServerTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudConfigServerTestApplication.class, args);
	}

}
