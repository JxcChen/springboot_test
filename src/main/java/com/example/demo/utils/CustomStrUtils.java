package com.example.demo.utils;

import com.example.demo.common.ServiceException;

import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/5/11 11:13
 * @Description: TODO
 */
public class CustomStrUtils {
    public static String list2String(List<Integer> list) {
        if (list == null || list.size()==0)
            return "集合数据为空";
        // 将集合转换成字符串 去掉[]
        return list.toString().replace("[","").replace("]","");
    }

    /**
     * 获取base_url=host+porty
     * @param url 请求url
     * @return
     */
    public static String getBaseUrl(String url) {
        if (url == null)
            throw new ServiceException("请求url不能为空");
        // 获取到域 和 端口号
        String requestMethod = "";
        String hostAndPort = "";
        if (url.contains("://")){
            requestMethod = url.substring(0,url.indexOf("://")+3);
            hostAndPort = url.substring(url.indexOf("://")+3);
            if (url.contains("/"))
                hostAndPort = hostAndPort.substring(0,hostAndPort.indexOf("/"));
        }

        return requestMethod+hostAndPort;
    }

    public static boolean isEmpty(String str){

        return str == null || str.equals("");
    }
}
