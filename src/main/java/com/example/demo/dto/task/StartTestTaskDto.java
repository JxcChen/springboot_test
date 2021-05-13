package com.example.demo.dto.task;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/5/12 16:51
 * @Description: TODO
 */

@ApiModel(value = "开始测试的数据传输对对象")
@Data
public class StartTestTaskDto extends BaseDto {

    @ApiModelProperty(value = "测试任务id")
    private Integer taskId;

    @ApiModelProperty(value = "测试命令",example = "以任务中储存的测试命令为主")
    private String testCommand;
}
