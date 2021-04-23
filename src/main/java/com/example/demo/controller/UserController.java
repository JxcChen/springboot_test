package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.ResultDto;
import com.example.demo.dto.AddUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.UserService;
import com.example.demo.common.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/4/16 8:30
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "spring") // 指定类全局访问路径
@Api(tags = "SpringBoot Test API")
@Slf4j
public class UserController {
    @Value("${spring.result}")
    public String value;

    @Autowired
    private UserService userService;

    // RequestMapping指定访问路径和请求方式 {userId}表示请求所需要的参数
    // @RequestMapping(value="login/{userId}",method = RequestMethod.GET)
    // GetMapping指定get请求方式 并设置访问路径
    @ApiOperation(value = "登录接口")
    @GetMapping("login/{userId}")
    public String login(@PathVariable int userId){
        System.out.println(userId);
        return "success";
    }

    /**
     * 注册接口
     * @param addUserDto 注册用户信息
     * @return 注册结果
     */
    @PostMapping("register")
    public ResultDto<AddUserDto> register(@RequestBody AddUserDto addUserDto){
        // 对用户名和密码进行非空判断
        if (addUserDto.getUserName()==null || addUserDto.getUserName().equals(""))
            return ResultDto.fail("用户名不能为空");
        if (addUserDto.getPassword()==null || addUserDto.getPassword().equals(""))
            return ResultDto.fail("密码不能为空");
        log.info("新用户注册 用户名："+addUserDto.getUserName() + " 密码："+addUserDto.getPassword());
        return userService.save(addUserDto);
    }


    @PutMapping("updateUser")
    public ResultDto<HogwartsTestUser> updateUser(@RequestBody UpdateUserDto updateUserDto){
        if(updateUserDto.getId() == 0){
            return ResultDto.fail("必须传入用户ID");
        }
        log.info("更新用户信息" + JSONObject.toJSONString(updateUserDto));
        return userService.updateUser(updateUserDto);
    }

    @GetMapping("getUser")
    public ResultDto<List<HogwartsTestUser>> getUserByName(@RequestParam(value = "userName") String userName){
        if (userName == null || userName.equals(""))
            return ResultDto.fail("用户名不能为空");
        log.info("根据名字查询用户 用户名：" + userName);
        return userService.getUserByName(userName);
    }


    @GetMapping("getUserId")
    public String getUserId(@RequestParam int userId){
        return "success"+userId+value;
    }




//    @RequestMapping(value="loginPost",method = RequestMethod.POST)
    @PostMapping("loginPost")
    public ResultDto loginPost(@RequestBody UserDto userDto) throws Exception {
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getName());

        if (userDto.getName().contains("err")){
            throw new ServiceException("用户名错误",new NullPointerException());
        }
        if (userDto.getName().contains("er2")){
//            throw new ServiceException("用户名有误");
            throw new NullPointerException();
        }
        // 使用统一响应对象进行数据返回
        return ResultDto.success("success",userDto) ;
    }


    @PostMapping("login")
    @ApiOperation(value = "登录服务方法")
    public String loginService(@RequestBody UserDto userDto){
        String result = userService.login(userDto);
        return "success   "+result;
    }
}
