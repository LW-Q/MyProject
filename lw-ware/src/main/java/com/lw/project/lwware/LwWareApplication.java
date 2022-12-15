package com.lw.project.lwware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.lw.project.lwware.feign")
@SpringBootApplication
public class LwWareApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwWareApplication.class, args);
	}

}
