package com.example.demo.dto.jenkins;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/4/28 14:39
 * @Description: 添加jenkins任务的数据传输对象
 */
@Data
@ApiModel(value = "添加jenkins任务的数据传输对象")
public class UpdateJenkinsDto extends BaseDto {

    /**
     * 主键
     */
    @ApiModelProperty(value="Jenkins主键",required=true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value="Jenkins名称")
    private String name;

    /**
     * 测试命令
     */
    @ApiModelProperty(value="测试命令前缀")
    private String testCommand;

    /**
     * Jenkins的baseUrl
     */
    @ApiModelProperty(value="Jenkins的baseUrl")
    private String url;

    /**
     * 用户名
     */
    @ApiModelProperty(value="Jenkins用户名称")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value="Jenkins用户密码")
    private String password;

    /**
     * 备注
     */
    @ApiModelProperty(value="Jenkins备注")
    private String remark;

    /**
     * 创建人id
     */
    @ApiModelProperty(value="创建人的id",example = "不需要手动填写")
    private Integer createUserId;

    /**
     *
     */
    @ApiModelProperty(value="是否设置为默认服务器 1 是 0 否")
    private Integer defaultFlag = 0;

    /**
     *
     */
    @ApiModelProperty(value="命令运行的测试用例类型  1 文本 2 文件")
    private Byte commandRunCaseType;

    /**
     *
     */
    @ApiModelProperty(value="测试用例后缀名 如果case为文件时，此处必填")
    private String commandRunCaseSuffix;



}
