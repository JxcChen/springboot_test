package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.*;
import com.example.demo.dto.user.AddUserDto;
import com.example.demo.dto.user.LoginUserDto;
import com.example.demo.dto.user.UpdateUserDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private TokenDb tokenDb;

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
    @ApiOperation(value = "注册接口")
    public ResultDto<HogwartsTestUser> register(@RequestBody AddUserDto addUserDto){
        // 对用户名和密码进行非空判断
        if (addUserDto.getUserName()==null || addUserDto.getUserName().equals(""))
            return ResultDto.fail("用户名不能为空");
        if (addUserDto.getPassword()==null || addUserDto.getPassword().equals(""))
            return ResultDto.fail("密码不能为空");
        log.info("新用户注册 用户名："+addUserDto.getUserName() + " 密码："+addUserDto.getPassword());
        return userService.save(addUserDto);
    }


    /**
     * 登录接口
     * @param loginUserDto
     * @return
     */
    @PostMapping("userLogin")
    @ApiOperation(value = "登录接口")
    public ResultDto<Token> userLogin(@RequestBody LoginUserDto loginUserDto){
        if (loginUserDto.getUserName() == null || loginUserDto.getUserName().equals(""))
            return ResultDto.fail("用户名不能为空");
        if (loginUserDto.getPassword() == null || loginUserDto.getPassword().equals(""))
            return ResultDto.fail("密码不能为空");
        return userService.login(loginUserDto);
    }

    /**
     * 退出登录接口
     * @param request http请求
     * @return
     */
    @DeleteMapping("/signOut")
    @ApiOperation(value = "退出登录接口")
    public ResultDto signOut(HttpServletRequest request){
        // 获取到用户token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);
        // 判断用户是否已经登录
        boolean isLogin = tokenDb.isLogin(token);
        if (!isLogin){
            return ResultDto.fail("用户未登录");
        }
        tokenDb.removeTokenDto(token);
        return ResultDto.success("退出成功");
    }

    /**
     * 更新用户信息
     * @param updateUserDto 需要更新的用户信息
     * @return
     */
    @PutMapping("updateUser")
    @ApiOperation(value = "更新用户数据接口")
    public ResultDto<HogwartsTestUser> updateUser(@RequestBody UpdateUserDto updateUserDto){
        if(updateUserDto.getId() == 0){
            return ResultDto.fail("必须传入用户ID");
        }
        log.info("更新用户信息" + JSONObject.toJSONString(updateUserDto));
        return userService.updateUser(updateUserDto);
    }

    /**
     * 根据用户名获取到用户信息 可进行模糊匹配
     * @param userName 用户名
     * @return 用户信息
     */
    @GetMapping("getUser")
    public ResultDto<List<HogwartsTestUser>> getUserByName(@RequestParam(value = "userName") String userName){
        if (userName == null || userName.equals(""))
            return ResultDto.fail("用户名不能为空");
        log.info("根据名字查询用户 用户名：" + userName);
        return userService.getUserByName(userName);
    }


    @DeleteMapping("deleteUser")
    public ResultDto deleteUserById(@RequestParam(value = "userId") Integer userId){
        if(userId == 0){
            return ResultDto.fail("请输入要删除的用户ID");
        }
        log.info("删除ID为 "+userId+"的用户信息");
        return userService.deleteUser(userId);
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
