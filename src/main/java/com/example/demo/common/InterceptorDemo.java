package com.example.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: JJJJ
 * @date:2021/4/21 8:56
 * @Description: TODO
 */
@Component
@Slf4j
public class InterceptorDemo implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("=========preHandle=========");
        log.info("request.getRequestURI() : "+request.getRequestURI());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        log.info("=========postHandle=========");
        log.info("request.getRequestURI() : "+request.getRequestURI());

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.info("=========afterCompletion=========");
        log.info("request.getRequestURI() : "+request.getRequestURI());
    }
}
