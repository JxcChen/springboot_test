package com.example.demo.dto;

import com.example.demo.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "新增用户")
public class UpdateUserDto extends BaseEntityNew {
    /**
     * 主键
     */
    @ApiModelProperty(value = "用户ID",required = true,example = "1")
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",example = "rose")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
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