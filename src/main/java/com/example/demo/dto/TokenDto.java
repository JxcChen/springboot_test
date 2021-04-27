package com.example.demo.dto;

import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/4/27 10:05
 * @Description: token数据保存
 */
@Data
public class TokenDto {
    private Integer userID;
    private String userName;
    private Integer defaultJenkinsId;
    private String token;
}
