package com.example.demo.common;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: JJJJ
 * @date:2021/4/22 11:55
 * @Description: 分页请求对象声明
 */

/**
 * 分页请求对象
 * @param <Dto>
 */
@ApiModel(value = "列表查询的分页参数",description = "请求参数类")
@Data
public class PageTableRequest<Dto extends BaseDto> implements Serializable {

    @ApiModelProperty(value = "页码",required = true,example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "每页数据量",required = true,example = "10")
    private Integer pageSize;
    @ApiModelProperty(value = "特定查询参数",required = true,example = "status = 1")
    private Dto param;

}
