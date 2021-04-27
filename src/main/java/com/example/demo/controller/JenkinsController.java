package com.example.demo.controller;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.service.JenkinsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private JenkinsService jenkinsService;
    public ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception {
        log.info("在jenkins中新创建一个名为"+jenkinsParamDto.getJobName()+"的job");
        return jenkinsService.createJob(jenkinsParamDto);
    }
}
