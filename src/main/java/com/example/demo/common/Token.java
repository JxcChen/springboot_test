package com.example.demo.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: JJJJ
 * @date:2021/4/28 9:47
 * @Description: 储存token和到期时间
 */
@Data
public class Token implements Serializable {

    private String token;
    /**
     * 设置到期时间
     */
    private Date expireTime;

}
