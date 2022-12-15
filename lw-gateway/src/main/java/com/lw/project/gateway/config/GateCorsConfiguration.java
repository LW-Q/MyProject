package com.lw.project.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GateCorsConfiguration {
    @Bean
    public CorsWebFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**",configuration);
        return new CorsWebFilter(source);
    }
}
