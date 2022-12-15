package com.lw.project.lwproduct.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement // 开始事务
@MapperScan(basePackages = "com.lw.project.lwproduct.dao")
public class ProductMyBatisConfig {
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
