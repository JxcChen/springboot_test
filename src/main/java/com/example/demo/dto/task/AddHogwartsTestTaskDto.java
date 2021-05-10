package com.example.demo.dto.task;

import com.example.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@ApiModel(value = "添加测试任务的数据传输对象")
@Data
public class AddHogwartsTestTaskDto extends BaseEntityNew {

    /**
     * 名称
     */
    @ApiModelProperty(value = "测试任务名称" ,required = true)
    private String name;

    /**
     * 运行测试的Jenkins服务器id
     */
    @ApiModelProperty(value = "运行测试的Jenkins服务器id",required = true)
    private Integer testJenkinsId;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;


    /**
     * 创建者id
     */
    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;


}