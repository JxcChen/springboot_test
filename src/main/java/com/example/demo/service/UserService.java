package com.example.demo.service;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.AddUserDto;
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


    public String login(UserDto userDto);

    ResultDto<AddUserDto> save(AddUserDto addUserDto);

    ResultDto<HogwartsTestUser> updateUser(UpdateUserDto updateUserDto);

    ResultDto<List<HogwartsTestUser>> getUserByName(String userName);
}
