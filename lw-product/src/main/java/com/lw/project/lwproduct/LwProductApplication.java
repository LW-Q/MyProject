package com.lw.project.lwproduct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/*
* 1、整合MybatisPlus
*
* */
@EnableFeignClients(basePackages = "com.lw.project.lwproduct.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class LwProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwProductApplication.class, args);
	}

}
