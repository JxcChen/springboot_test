package com.example.demo.common;

import com.example.demo.dto.TokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: JJJJ
 * @date:2021/4/21 8:56
 * @Description: 进行登录拦截
 */
@Component
@Slf4j
public class InterceptorDemo implements HandlerInterceptor {

    @Autowired
    private TokenDb tokenDb;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、从请求的header获取客户端添加的token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        // 获取当前请求路径 uri 可根据uri来识别是否需要放行
        String requestURI = request.getRequestURI();

        // 判断是否是swagger地址 如果是则放行
        boolean isSwaggerFlag = requestURI.contains("swagger")
                || requestURI.contains("/error")
                || requestURI.contains("csrf")
                || requestURI.contains("/favicon.ico")
                || requestURI.contains("/report/showMapLocal")
                || requestURI.equals("/");
        if (isSwaggerFlag)
            return true;

        // 如果无token则返回错误
        if (token == null || token.equals("")){
            response.setStatus(401);
            ServiceException.throwEx("客户端未传token");
        }
        // 根据token获取tokenDto
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 查看是否有存入用户信息
        if(userInfo == null){
            response.setStatus(401);
            ServiceException.throwEx("用户未登录");
        }
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
