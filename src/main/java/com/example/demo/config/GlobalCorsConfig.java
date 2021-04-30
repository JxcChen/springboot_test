package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author: JJJJ
 * @date:2021/4/22 9:49
 * @Description: 允许跨域配置
 */

@Configuration
public class GlobalCorsConfig {
    @Bean
    public CorsFilter corsFilter(){
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOriginPattern("*"); // 允许向服务器请求的URI，*标识全部允许.allowedOriginPatterns
        config.addAllowedHeader("*");// 允许访问的header * 表示全部允许
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），在这段时间内跨域请求不在预检
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**",config);
        return new CorsFilter(source);
    }
}
