package com.example.demo.common;

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.TokenDto;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: JJJJ
 * @date:2021/4/27 10:02
 * @Description: 用户缓存token数据 类似于redis将数据缓存在内存当中
 */

@Component
public class TokenDb {
    // 1 定义用户缓存token的map（key=token）
    private Map<String, TokenDto> tokenMap = new HashMap<>();
    // 2 获取在线用户数
    public Integer getOnlineUserNum(){
        return tokenMap.size();
    }
    // 3 根据token获取tokenDto
    public TokenDto getUserInfo(String token){
        if (token == null)
            return null;
        return tokenMap.get(token);
    }
    // 4 用户登录时新增token和tokenDto
    public void addUserInfo(String token,TokenDto tokenDto){
        if (token == null)
            return ;
        tokenMap.put(token,tokenDto);
    }
    // 5 用户登出时移除tokenDto
    public void removeTokenDto(String token){
        if(token !=null)
            tokenMap.remove(token);
    }
    // 6 判断用户是否登录
    public boolean isLogin(String token){
        return tokenMap.get(token) != null;
    }
}
