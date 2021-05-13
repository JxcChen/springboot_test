package com.example.demo.common;

import com.example.demo.dao.HogwartsTestJenkinsMapper;
import com.example.demo.dao.HogwartsTestTaskMapper;
import com.example.demo.dao.HogwartsTestUserMapper;
import com.example.demo.dto.TokenDto;
import com.example.demo.dto.jenkins.OperateJenkinsDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.example.demo.entity.HogwartsTestTask;
import com.example.demo.entity.HogwartsTestUser;
import com.example.demo.utils.CustomStrUtils;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.JenkinsUtil;
import com.offbytwo.jenkins.model.Job;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.util.Map;

/**
 * author: JJJJ
 * date:2021/5/13 8:31
 * Description: TODO
 */

@Component
@Slf4j
public class JenkinsClient {

    @Autowired
    HogwartsTestTaskMapper testTaskMapper;

    @Autowired
    HogwartsTestUserMapper userMapper;

    @Autowired
    HogwartsTestJenkinsMapper jenkinsMapper;


    /**
     * 执行jenkins
     * @param operateJenkinsDto 执行jenkins所需数据传输对象
     */
    public ResultDto operateJenkins(OperateJenkinsDto operateJenkinsDto) throws Exception {
        TokenDto tokenDto = operateJenkinsDto.getTokenDto();
        HogwartsTestJenkins hogwartsTestJenkins = operateJenkinsDto.getHogwartsTestJenkins();
        Map<String, String> params = operateJenkinsDto.getParams();
        // 获取数据库中对应的用户信息
        HogwartsTestUser hogwartsTestUser = userMapper.selectOneByUserName(tokenDto.getUserName());


        // 声明jobName  默认名+用户id
        String jobName = "AITest_Project_" + tokenDto.getUserID();
        // 声明jobSign
        String jobSign = jobName.substring(0,jobName.lastIndexOf("_"));


        // 获取创建job的xml
        // 1 获取classPathResource
        ClassPathResource classPathResource = new ClassPathResource("jenkins_job_file/" + jobSign + ".xml");
        // 获取输入流
        InputStream inputStream = classPathResource.getInputStream();
        // 将输入流转化成文本信息
        String jobXmlText = FileUtil.getText(inputStream);

        // 获取user表中 start_test_job_name 存在则代表已经创建过job 只能进行更新,不存在则进行创建
        String startTestJobName = hogwartsTestUser.getStartTestJobName();
        Job jenkinsJob;
        if (CustomStrUtils.isEmpty(startTestJobName)){
            // 未创建过job 进行新建
            jenkinsJob = JenkinsUtil.createOrUpdateJob(jobName, jobXmlText, hogwartsTestJenkins, 1);
            // 更新数据库字段
            hogwartsTestUser.setStartTestJobName(jobName);
            userMapper.updateByPrimaryKeySelective(hogwartsTestUser);
        }else {
            jenkinsJob = JenkinsUtil.createOrUpdateJob(jobName, jobXmlText, hogwartsTestJenkins, 2);
        }
        // 进行项目构建
        try {
            return JenkinsUtil.buildJOb(jenkinsJob,params);
        } catch (Exception e) {
            String tips = "操作Jenkins的Job异常"+e.getMessage();
            log.error(tips,e);
            throw new ServiceException(tips);
        }

    }
}
