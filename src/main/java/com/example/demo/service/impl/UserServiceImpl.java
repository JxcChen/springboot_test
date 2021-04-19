package com.example.demo.service.impl;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author: JJJJ
 * @date:2021/4/19 8:52
 * @Description: TODO
 */
// @Component("userServiceImpl")
@Service
public class UserServiceImpl implements UserService {

    @Override
    public String login(UserDto userDto) {
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getName());
        return "成功登录";
    }
}
