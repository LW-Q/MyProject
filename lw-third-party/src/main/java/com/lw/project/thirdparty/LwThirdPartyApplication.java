package com.lw.project.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class LwThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(LwThirdPartyApplication.class, args);
    }

}
