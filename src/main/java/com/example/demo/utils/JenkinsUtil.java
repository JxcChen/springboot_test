package com.example.demo.utils;

import com.example.demo.dto.JenkinsParamDto;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author: JJJJ
 * @date:2021/4/26 8:21
 * @Description: 调用jenkins
 */
public class JenkinsUtil {

    private static JenkinsHttpClient jenkinsClient;
    private static String jobConfigXml;

    public static void init() throws Exception {
        // 读取xml配置文件
        ClassPathResource classPathResource = new ClassPathResource("jenkins_job_file/spring_test.xml");
        InputStream inputStream = classPathResource.getInputStream();
        jobConfigXml = FileUtil.getText(inputStream);
        // Jenkins Client所需配置
        String baseUrl = "http://159.75.6.218:8080/";
        String name = "CHNJX";
        String password = "aaa11xuan";
        // 声明jenkins client
        jenkinsClient = new JenkinsHttpClient(new URI(baseUrl),name,password);
    }

    public static void createJob(JenkinsParamDto jenkinsParamDto) throws Exception {
        init();
        String jobName = jenkinsParamDto.getJobName();

        // 声明 Jenkins Server
        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsClient);
        JobWithDetails serverJob = jenkinsServer.getJob(jobName);
        if (serverJob != null){
            jenkinsServer.deleteJob(jobName,true);
        }
        // 利用Server创建一个job
        jenkinsServer.createJob(jobName, jobConfigXml,true);
        // 获取job集合
        Map<String, Job> jobs = jenkinsServer.getJobs();
        // 获取对应的job
        Job job = jobs.get(jobName);
        // 设置参数
        HashMap<String,String> paramMap = new HashMap<>();
        paramMap.put("userName",jenkinsParamDto.getUserName());
        paramMap.put("description",jenkinsParamDto.getDescription());
        // 进行构建
        job.build(paramMap,true);
    }
}
