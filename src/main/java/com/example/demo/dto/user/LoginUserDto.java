package com.example.demo.dto.user;

import com.example.demo.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/4/25 9:16
 * @Description: 登录所需要的信息
 */
@Data
@ApiModel(value = "登录用户",description = "用户登录接口数据封装")
public class LoginUserDto extends BaseDto {
    @ApiModelProperty(value = "用户名",example = "jack",required = true)
    private String userName;
    @ApiModelProperty(value = "密码",example = "123",required = true)
    private String password;
}

