package com.example.demo.utils;

import com.example.demo.common.ServiceException;
import com.example.demo.entity.HogwartsTestJenkins;

/**
 * author: JJJJ
 * date:2021/5/13 16:03
 * Description: 测试报告相关方法
 */
public class ReportUtils {


    /**
     * 获取测试报告路劲
     * @param buildUrl jenkins任务构建路径
     * @param testJenkins jenkins信息存储对象
     * @param isAutoLoginFlag 是否实现自动登录 1 是 2 否
     */
    public static String getReportUrl(String buildUrl, HogwartsTestJenkins testJenkins, int isAutoLoginFlag) {
        if (CustomStrUtils.isEmpty(buildUrl))
            throw new ServiceException("jenkins构建路径不能为空");
        String allureReportUrl = "";
        if (isAutoLoginFlag == 1){
//            allureReportUrl += autoLoginUrl(testJenkins);
//            allureReportUrl += buildUrl.substring(buildUrl.indexOf("/job"))+"allure/";
            allureReportUrl = buildUrl + "allure/";
        }
        if (isAutoLoginFlag == 2)
            allureReportUrl = buildUrl + "allure/";
        return allureReportUrl;
    }

    /**
     * 获取报告拼接自动登录命令
     * @param testJenkins 需要jenkins用户名和密码
     */
    private static String autoLoginUrl(HogwartsTestJenkins testJenkins) {
        String jenkinsUrl = testJenkins.getUrl();
        String userName = testJenkins.getUserName();
        String password = testJenkins.getPassword();
        // 拼接自动登录需要的url
        return jenkinsUrl + "j_acegi_security_check?j_username="+userName
                +"&j_password="+password+"&Submit=登录&remember_me=on"+"&from=";
    }
}
