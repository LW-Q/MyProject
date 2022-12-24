package com.lw.project.lwproduct.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyRedissonConfig {
    /*
    * 所有对Redisson的使用都是通过RedissonClient对象
    * */
    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson() throws IOException {
        // 创建配置
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.56.10:6379");
        // 根据config创建出RedissonClient示例
        return Redisson.create(config);
    }
}
