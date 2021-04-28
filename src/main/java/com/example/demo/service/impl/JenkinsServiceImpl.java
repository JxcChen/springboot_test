package com.example.demo.service.impl;

import com.example.demo.common.ResultDto;
import com.example.demo.common.TokenDb;
import com.example.demo.dao.HogwartsTestJenkinsMapper;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.AddJenkinsDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.service.JenkinsService;
import com.example.demo.utils.JenkinsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author: JJJJ
 * @date:2021/4/26 11:19
 * @Description: TODO
 */
@Service
public class JenkinsServiceImpl implements JenkinsService {
    private HogwartsTestJenkins hogwartsTestJenkins = new HogwartsTestJenkins();
    @Autowired
    private HogwartsTestJenkinsMapper hogwartsTestJenkinsMapper;
    @Autowired
    private HogwartsTestUserMapper hogwartsTestUserMapper;
    @Autowired
    private TokenDb tokenDb;

    @Override
    public ResultDto createJob(JenkinsParamDto jenkinsParamDto) throws Exception {
        JenkinsUtil.createJob(jenkinsParamDto);
        return ResultDto.success("构建成功");
    }

    @Override
    public ResultDto<HogwartsTestJenkins> save(TokenDto userInfo, AddJenkinsDto addJenkinsDto) {
        // 1、 将可能存在的.yml .json 修改成yml和json
        String caseSuffix = addJenkinsDto.getCommandRunCaseSuffix();

        if (caseSuffix != null && caseSuffix.startsWith(".")){
            String replaceSuffix = caseSuffix.replace(".", "");
            addJenkinsDto.setCommandRunCaseSuffix(replaceSuffix);
        }


        // 将addJenkinsDto数据赋给hogwartsTestJenkins
        BeanUtils.copyProperties(addJenkinsDto,hogwartsTestJenkins);
        System.out.println(hogwartsTestJenkins.getName()+"======================");
        System.out.println("========="+ hogwartsTestJenkins.getCommandRunCaseType());
        // 设置创建和更新时间
        hogwartsTestJenkins.setCreateTime(new Date());
        hogwartsTestJenkins.setUpdateTime(new Date());
        // 数据落库
        int insert = hogwartsTestJenkinsMapper.insertUseGeneratedKeys(hogwartsTestJenkins);

        // 查看jenkins默认选项是否勾选
        Integer defaultFlag = hogwartsTestJenkins.getDefaultFlag();
        if (defaultFlag != null && defaultFlag == 1){
            // 若勾选了默认 则需要修改用户表中的默认jenkinsId
            // 对用户表进行更新
            HogwartsTestUser hogwartsTestUser = new HogwartsTestUser();
            hogwartsTestUser.setId(hogwartsTestJenkins.getCreateUserId());
            hogwartsTestUser.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            hogwartsTestUserMapper.updateByPrimaryKeySelective(hogwartsTestUser);

            // 更新token中的jenkinsId
            userInfo.setDefaultJenkinsId(hogwartsTestJenkins.getId());
            tokenDb.addUserInfo(hogwartsTestUser.getId(),userInfo.getToken(),userInfo);
        }
        return ResultDto.success("添加成功",hogwartsTestJenkins);
    }
}
