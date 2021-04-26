package com.example.demo.service.impl;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.service.JenkinsService;
import com.example.demo.utils.JenkinsUtil;
import org.springframework.stereotype.Service;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:19
 * @Description: TODO
 */
@Service
public class JenkinsServiceImpl implements JenkinsService {
    @Override
    public ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception {
        JenkinsUtil.createJob(jenkinsParamDto);
        return ResultDto.success("构建成功");
    }
}
