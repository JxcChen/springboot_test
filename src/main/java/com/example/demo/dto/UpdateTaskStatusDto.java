package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/5/12 19:19
 * @Description: 更新任务状态数据传输对象
 */
@Data
@ApiModel(value = "更新任务状态数据传输对象")
public class UpdateTaskStatusDto {

    @ApiModelProperty(value = "测试任务id",required = true)
    private Integer taskId;

    @ApiModelProperty(value = "任务状态",required = true)
    private Integer status;

    @ApiModelProperty(value = "构建路径",required = true)
    private String buildUrl;
}
