package com.lw.project.lwware.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.lw.project.lwware.dao")
@Configuration
public class WareMyBatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 超过最大页，true回到首页，默认false
        paginationInterceptor.setOverflow(true);

        // 限制最大单页数量
        paginationInterceptor.setLimit(1000L);
        return paginationInterceptor;
    }
}

