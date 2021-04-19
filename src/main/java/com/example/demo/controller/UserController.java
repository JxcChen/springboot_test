package com.example.demo.controller;

import com.example.demo.dto.UserDto;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author: JJJJ
 * @date:2021/4/16 8:30
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "spring") // 指定类全局访问路径
public class UserController {

    // RequestMapping指定访问路径和请求方式 {userId}表示请求所需要的参数
    // @RequestMapping(value="login/{userId}",method = RequestMethod.GET)
    // GetMapping指定get请求方式 并设置访问路径
    @GetMapping("login/{userId}")
    public String login(@PathVariable int userId){
        System.out.println(userId);
        return "success";
    }

    @Value("${spring.result}")
    public String value;

    @GetMapping("getUserId")
    public String getUserId(@RequestParam int userId){
        return "success"+userId+value;
    }

//    @RequestMapping(value="loginPost",method = RequestMethod.POST)
    @PostMapping("loginPost")
    public String loginPost(@RequestBody UserDto userDto){
        System.out.println(userDto.getUserId());
        System.out.println(userDto.getName());
        return "success";
    }
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public String loginService(@RequestBody UserDto userDto){
        String result = userService.login(userDto);
        return "success   "+result;
    }
}
