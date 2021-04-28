package com.example.demo.service;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.entity.HogwartsTestJenkins;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:17
 * @Description: TODO
 */
public interface JenkinsService {
    ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception;

    ResultDto<HogwartsTestJenkins> save(TokenDto userInfo, AddJenkinsDto addJenkinsDto);
}
