package com.example.demo.dto.jenkins;

import com.example.demo.dto.TokenDto;
import com.example.demo.entity.HogwartsTestJenkins;
import lombok.Data;

import java.util.Map;

/**
 * @author: JJJJ
 * @date:2021/5/12 20:27
 * @Description: 进行jenkins构建需要携带的信息
 */
@Data
public class OperateJenkinsDto {

    // 需要通过用户id获取本地jenkins
    private TokenDto tokenDto;

    // 存储需要运行的jenkins数据
    private HogwartsTestJenkins hogwartsTestJenkins;

    // 构建jenkins参数
    private Map<String, String> params;

}
