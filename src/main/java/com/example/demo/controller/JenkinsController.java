package com.example.demo.controller;

import com.example.demo.common.ResultDto;
import com.example.demo.common.TokenDb;
import com.example.demo.common.UserConstants;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.service.JenkinsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:07
 * @Description: 对jenkins进行操控
 */

@RestController
@RequestMapping(value = "spring/jenkins")
@Api(value = "jenkins api")
@Slf4j
public class JenkinsController {
    @Autowired
    private JenkinsService jenkinsService;

    @Autowired
    private TokenDb tokenDb;
    @PostMapping("/addJenkins")
    public ResultDto<HogwartsTestJenkins> addJenkins(@RequestBody AddJenkinsDto addJenkinsDto, HttpServletRequest request) throws Exception {
        log.info("新增jenkins");
        // 先对必要信息进行非空判断
        if (addJenkinsDto == null){
            return ResultDto.fail("jenkins相关信息不能为空");
        }
        if(addJenkinsDto.getName() == null || addJenkinsDto.getName().equals(""))
            return ResultDto.fail("jenkins名称不能为空");
        // 获取到token
        String token = request.getHeader(UserConstants.LOGIN_TOKEN);

        // 根据token获取到tokenDto
        TokenDto userInfo = tokenDb.getUserInfo(token);
        // 设置jenkins createUserId
        addJenkinsDto.setCreateUserId(userInfo.getUserID());

        return jenkinsService.save(userInfo,addJenkinsDto);
    }
}
