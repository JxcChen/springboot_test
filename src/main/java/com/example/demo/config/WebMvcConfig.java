package com.example.demo.config;

import com.example.demo.common.InterceptorDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/21 17:59
 * @Description: TODO
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Autowired
    private InterceptorDemo interceptorDemo;


    // addPathPatterns("/**") 表示拦截所有路劲请求
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorDemo).addPathPatterns("/**").excludePathPatterns("/spring/loginPost");
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(messageConverter());
    }

    public HttpMessageConverter<String> messageConverter(){
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }
}
