package com.springcloud.EurekaClientTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * SpringBoot Service 실행하기 위해...
 * - intelliJ - Application Configuration 조절(Vm arguments 또는 Program arguments 에 server.port = xxxx)
 * - "./gradlew bootRun --args='--server.port=9003' " gradlew 명령어로 직접 빌드하여 실행
 * - "./gradlew bootJar" 로 executable jar 을 만든 후 "java -jar -Dserver.port=9004 ~~SNAPSHOT.jar" 로 server-port주어 실행
 * - server.port: 0 (Random Port 이용) -> instance port는 yaml 파일 properties info로 지정되므로 instance-id 를 추가로 주어야 한다
 */

/*
 * This is springcloud-user-service
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EurekaClientTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaClientTestApplication.class, args);
	}

}
