package com.example.demo.utils;

import com.example.demo.common.UserConstants;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author: JJJJ
 * @date:2021/4/28 8:31
 * @Description: 对数据进行MD5加密
 */
public class MD5Util {

    public static String Md5Encryption(String message){
        // 对传入来的密码 加上特定数据和时间戳一起进行md5加密
        return DigestUtils.md5DigestAsHex((message + UserConstants.MD5_SIGN).getBytes());
    }

}
