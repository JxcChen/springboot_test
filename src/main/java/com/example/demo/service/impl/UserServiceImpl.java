package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.ResultDto;
import com.example.demo.common.Token;
import com.example.demo.common.TokenDb;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dto.*;
import com.example.demo.dto.user.AddUserDto;
import com.example.demo.dto.user.LoginUserDto;
import com.example.demo.dto.user.UpdateUserDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.UserService;
import com.example.demo.utils.MD5Util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/19 8:52
 * @Description: 用户服务层
 */
// @Component("userServiceImpl")
@Service
public class UserServiceImpl implements UserService {

    private HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();

    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    @Override
    public String login(UserDto userDto) {
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getName());
        return "成功登录";
    }

    @Override
    public ResultDto<HogwartsTestUser> save(AddUserDto addUserDto) {

        // 1、先校验用户名唯一性
        hogwartsTestUser.setUserName(addUserDto.getUserName());
        List<HogwartsTestUser> isExist = hogwartsTestUserMapper.select(hogwartsTestUser);
        if (isExist != null && isExist.size() > 0){
            return ResultDto.fail("用户名已经存在");
        }
        // 用户名不存在的前提下再进行接下来的步骤
        ///2、对密码进行md5加密
        // 获取md5加密后的密码
        String newPassword = MD5Util.Md5Encryption(addUserDto.getPassword()+addUserDto.getUserName());
        // 将加密后的密码存回数据传输对象中
        addUserDto.setPassword(newPassword);
        // 4、将传入的dto类 信息复制给实体类
        BeanUtils.copyProperties(addUserDto, this.hogwartsTestUser);
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());
        // 5、使用hogwartsTestUserMapper插入数据
        hogwartsTestUserMapper.insert(hogwartsTestUser);
        System.out.println(JSONObject.toJSONString(hogwartsTestUser));
        return ResultDto.success("注册成功", hogwartsTestUser);
    }

    @Override
    public ResultDto<Token> login(LoginUserDto loginUserDto) {
        // 1、校验用户名是否存在
        HogwartsTestUser loginUser = hogwartsTestUserMapper.selectOneByUserName(loginUserDto.getUserName());
        if(loginUser == null)
            return ResultDto.fail("用户名或密码错误");
        // 2、对密码进行加密后 验证密码是否正确
        String newPassword = MD5Util.Md5Encryption(loginUserDto.getPassword()+loginUser.getUserName());
        if(!newPassword.equals(loginUser.getPassword())){
            return ResultDto.fail("用户名或密码错误");
        }
        // 3、密码验证通过 代表登录成功 需要创建token
        Token token = new Token();
        // 生成token数据
        String tokenStr = MD5Util.Md5Encryption(loginUser.getUserName()+loginUser.getPassword()+System.currentTimeMillis());
        token.setToken(tokenStr);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(tokenStr);
        tokenDto.setUserName(loginUser.getUserName());
        tokenDto.setUserID(loginUser.getId());
        tokenDto.setDefaultJenkinsId(loginUser.getDefaultJenkinsId());
        // 储存token数据
        // tokenDb将token缓存在内存当中 在拦截器中进行校验
        tokenDb.addUserInfo(loginUser.getId(),tokenStr,tokenDto);
        // 返回token数据给前端
        return ResultDto.success("登陆成功",token);
    }



    @Override
    public ResultDto<HogwartsTestUser> updateUser(UpdateUserDto updateUserDto) {
        BeanUtils.copyProperties(updateUserDto, hogwartsTestUser);
        hogwartsTestUser.setCreateTime(new Date());
        hogwartsTestUser.setUpdateTime(new Date());
        // 为空的字段不进行更新
        hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);
        return ResultDto.success("修改成功",hogwartsTestUser);
    }

    @Override
    public ResultDto<List<HogwartsTestUser>> getUserByName(String userName) {
        return ResultDto.success("成功",hogwartsTestUserMapper.getUserByName(userName));
    }

    @Override
    public ResultDto deleteUser(Integer userId) {
        hogwartsTestUserMapper.deleteByIds(userId.toString());
        return ResultDto.success("删除成功");
    }

}
