package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/4/26 10:25
 * @Description: jenkins创建job所需要的参数
 */
@Data
@ApiModel(value = "jenkins参数")
public class JenkinsParamDto {
    @ApiModelProperty(value = "userName",example = "jack")
    private String userName;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "项目名",required = true)
    private String jobName;
}
