package com.example.demo.dto.user;

import com.example.demo.dto.BaseDto;
import com.example.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@ApiModel(value = "新增用户")
public class AddUserDto extends BaseDto {
    /**
     * 主键
     */
    @ApiModelProperty(value = "用户ID",example = "1")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",required = true,example = "rose")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 自动生成用例job名称 不为空时表示已经创建job
     */
    @ApiModelProperty(value = "创建用例名称")
    private String autoCreateCaseJobName;

    /**
     * 执行测试job名称 不为空时表示已经创建job
     */
    @ApiModelProperty(value = "job名称")
    private String startTestJobName;

    /**
     * 默认Jenkins服务器
     */
    @ApiModelProperty(value = "Jenkins服务器")
    private Integer defaultJenkinsId;


}