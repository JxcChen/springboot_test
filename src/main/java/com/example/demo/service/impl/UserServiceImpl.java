package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.ResultDto;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dto.AddUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/19 8:52
 * @Description: TODO
 */
// @Component("userServiceImpl")
@Service
public class UserServiceImpl implements UserService {

    private HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;

    @Override
    public String login(UserDto userDto) {
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getName());
        return "成功登录";
    }

    @Override
    public ResultDto<AddUserDto> save(AddUserDto addUserDto) {
        // 将传入的dto类 信息复制给实体类
        BeanUtils.copyProperties(addUserDto, hogwartsTestUser);
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());
        // 使用hogwartsTestUserMapper插入数据
        hogwartsTestUserMapper.insert(hogwartsTestUser);
        System.out.println(JSONObject.toJSONString(hogwartsTestUser));
        return ResultDto.success("注册成功",addUserDto);
    }

    @Override
    public ResultDto<HogwartsTestUser> updateUser(UpdateUserDto updateUserDto) {
        BeanUtils.copyProperties(updateUserDto, hogwartsTestUser);
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());
        hogwartsTestUserMapper.updateByPrimaryKey(hogwartsTestUser);
        return ResultDto.success("修改成功",hogwartsTestUser);
    }

    @Override
    public ResultDto<List<HogwartsTestUser>> getUserByName(String userName) {
        return ResultDto.success("成功",hogwartsTestUserMapper.getUserByName(userName));
    }
}
