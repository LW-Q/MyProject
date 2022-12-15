package com.lw.project.lwcoupon;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class LwCouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(LwCouponApplication.class, args);
	}

}
