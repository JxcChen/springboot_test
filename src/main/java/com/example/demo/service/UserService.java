package com.example.demo.service;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.AddUserDto;
import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.HogwartsTestUser;

import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/19 8:50
 * @Description: TODO
 */
public interface UserService {


    String login(UserDto userDto);

    /**
     * 用户登录
     * @param loginUserDto 登录信息
     * @return 统一响应结果
     */
    ResultDto<HogwartsTestUser> login(LoginUserDto loginUserDto);

    /**
     * 注册用户
     * @param addUserDto 要注册的用户信息
     * @return 统一响应对象
     */
    ResultDto<AddUserDto> save(AddUserDto addUserDto);

    /**
     * 更新用户信息
     * @param updateUserDto 需要更新的用户信息
     * @return 统一响应U对象
     */
    ResultDto<HogwartsTestUser> updateUser(UpdateUserDto updateUserDto);

    /**
     * 通过用户名获取用户信息  可进行模糊匹配
     * @param userName 用户名
     * @return 统一响应对象
     */
    ResultDto<List<HogwartsTestUser>> getUserByName(String userName);

    /**
     * 删除用户
     * @param userId 用户id
     * @return 统一响应对象
     */
    ResultDto deleteUser(Integer userId);
}
