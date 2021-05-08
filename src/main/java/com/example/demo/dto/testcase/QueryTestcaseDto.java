package com.example.demo.dto.testcase;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/5/8 14:29
 * @Description: TODO
 */
@Data
@ApiModel(value = "查询数据传输对象")
public class QueryTestcaseDto extends BaseDto {

    @ApiModelProperty(value = "创建者ID")
    private Integer userId;

    // 用例名称用于模糊查询用例列表
    @ApiModelProperty(value = "用例名称")
    private String caseName;
}
