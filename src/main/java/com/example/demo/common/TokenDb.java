package com.example.demo.common;

import com.example.demo.dto.TokenDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: JJJJ
 * @date:2021/4/27 10:02
 * @Description: 用户缓存token数据 类似于redis将数据缓存在内存当中
 */

@Component
public class TokenDb {
    // 1 定义用户缓存token的map（key=token） 还有 用户名对应的token map
    private Map<String, TokenDto> tokenMap = new HashMap<>();
    private Map<Integer, String> userTokenMap = new HashMap<>();
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
    public void addUserInfo(Integer userId,String token,TokenDto tokenDto){
        if (token == null)
            return ;
        // 先判断该用户是否已经登录
        if (userTokenMap.containsKey(userId)){
            // 若登陆则该用户原有token信息 将原有登录用户进行踢出
            removeTokenDto(userTokenMap.get(userId));
        }
        // 存储token
        userTokenMap.put(userId,token);
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
