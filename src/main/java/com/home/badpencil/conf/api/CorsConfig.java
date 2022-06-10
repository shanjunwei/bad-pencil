package com.home.badpencil.conf.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        /*允许访问的客户端域名*/
        config.addAllowedOrigin("*");
        /*是否允许请求带有验证信息*/
        config.setAllowCredentials(true);
        /*允许访问的方法名,GET POST等*/
        config.addAllowedMethod("*");
        /*允许服务端访问的客户端请求头*/
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}

