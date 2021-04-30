package com.example.demo.service;

import com.example.demo.common.PageTableRequest;
import com.example.demo.common.PageTableResponse;
import com.example.demo.common.ResultDto;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.dto.jenkins.QueryJenkinsListDto;
import com.example.demo.entity.HogwartsTestJenkins;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:17
 * @Description: TODO
 */
public interface JenkinsService {
    /**
     * 创建job
     * @param jenkinsParamDto 创建job传输数据对象
     * @return
     * @throws Exception
     */
    ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception;

    /**
     * 新增jenkins
     * @param userInfo tokenDb
     * @param addJenkinsDto 添加Jenkins数据传输对象
     * @return
     */
    ResultDto<HogwartsTestJenkins> save(TokenDto userInfo, AddJenkinsDto addJenkinsDto);

    /**
     * 分页查询Jenkins列表
     * @param pageTableRequest 分页查询数据
     * @return
     */
    ResultDto<PageTableResponse<HogwartsTestJenkins>> list(PageTableRequest<QueryJenkinsListDto> pageTableRequest);

    /**
     * 更新jenkins
     *
     * @param userInfo
     * @param hogwartsTestJenkins jenkins实体类
     * @return
     */
    ResultDto<HogwartsTestJenkins> update(TokenDto userInfo, HogwartsTestJenkins hogwartsTestJenkins);

    ResultDto delete(TokenDto userInfo, Integer jenkinsId);
}
