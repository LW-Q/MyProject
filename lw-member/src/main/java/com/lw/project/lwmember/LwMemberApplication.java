package com.lw.project.lwmember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.lw.project.lwmember.feign")
@EnableDiscoveryClient
public class LwMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwMemberApplication.class, args);
	}

}
