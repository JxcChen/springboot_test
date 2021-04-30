package com.example.demo.dto.jenkins;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/4/29 9:37
 * @Description: TODO
 */
@ApiModel(value = "查询jenkins列表数据传输对象")
@Data
public class QueryJenkinsListDto extends BaseDto {
    @ApiModelProperty(value = "Jenkins名称")
    private String name;

    @ApiModelProperty(value = "创建者Id")
    private Integer createUserId;

}
