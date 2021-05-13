package com.example.demo.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: JJJJ
 * @date:2021/5/11 10:43
 * @Description: TODO
 */
@Data
@ApiModel(value = "接收传输数据对象")
public class TestTaskDto {
    @ApiModelProperty(value = "新增测试任务数据传输对象",required = true)
    AddHogwartsTestTaskDto addHogwartsTestTaskDto = new AddHogwartsTestTaskDto();

    @ApiModelProperty(value = "需要执行的测试用例id列表",required = true)
    List<Integer> testTaskIdList = new ArrayList<>();
}
