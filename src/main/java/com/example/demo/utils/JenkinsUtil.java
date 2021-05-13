package com.example.demo.utils;

import com.example.demo.common.ResultDto;
import com.example.demo.dto.JenkinsParamDto;
import com.example.demo.entity.HogwartsTestJenkins;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author: JJJJ
 * @date:2021/4/26 8:21
 * @Description: 调用jenkins
 */
@Slf4j
public class JenkinsUtil {

    /**
     * 创建或更新jenkins job
     * @param jobName job名
     * @param jobXmlText 构建job的xml文件
     * @param hogwartsTestJenkins jenkins相关配置信息存储
     * @param createOrUpdateFlag 进行创建或更新的标记
     * @return
     * @throws Exception
     */
    public static Job createOrUpdateJob(String jobName, String jobXmlText, HogwartsTestJenkins hogwartsTestJenkins, int createOrUpdateFlag) throws Exception {
        // 获取jenkinsClient
        JenkinsHttpClient jenkinsClient = getJenkinsClient(hogwartsTestJenkins);
        // 声明jenkinsServer
        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsClient);
        if (createOrUpdateFlag == 1){
            // 创建job
            JobWithDetails serverJob = jenkinsServer.getJob(jobName);
            if (serverJob != null){
                // job已存在进行更新
                createOrUpdateFlag = 2;
            }
            // 创建job
            log.info("jenkins创建名字为: "+jobName+"的job");
            jenkinsServer.createJob(jobName, jobXmlText,true);
        }
        if (createOrUpdateFlag == 2){
            log.info("jenkins对名字为: "+jobName+"的job进行更新");
            jenkinsServer.updateJob(jobName,jobXmlText,true);
        }
        // 获取job集合
        Map<String, Job> jobs = jenkinsServer.getJobs();
        // 获取对应的job
        return jobs.get(jobName);

    }

    /**
     * 进行项目构建
     * @param job 需要构建的job
     * @param params 构建参数
     * @return
     * @throws IOException
     */
    public static ResultDto buildJOb(Job job,Map<String, String> params) throws IOException {
        if (job == null)
            return ResultDto.fail("项目不存在");
        // 进行构建
        if (params == null){
            job.build(true);
        }else {
            job.build(params,true);
        }
        return ResultDto.success("构建成功");
    }

    /**
     * 获取jenkinsClient
     * @param hogwartsTestJenkins 储存jenkinsClient所需要的信息
     * @return
     * @throws Exception
     */
    public static JenkinsHttpClient getJenkinsClient(HogwartsTestJenkins hogwartsTestJenkins) throws Exception {
        // Jenkins Client所需配置
        String baseUrl = hogwartsTestJenkins.getUrl();
        String name = hogwartsTestJenkins.getUserName();
        String password = hogwartsTestJenkins.getPassword();
        // 声明jenkins client
        JenkinsHttpClient jenkinsClient = new JenkinsHttpClient(new URI(baseUrl),name,password);
        return jenkinsClient;
    }

    public static void createOrUpdateJob(JenkinsParamDto jenkinsParamDto) {
    }
}
