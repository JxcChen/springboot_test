package com.example.demo.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: JJJJ
 * @date:2021/4/16 8:40
 * @Description: TODO
 */
//@Getter
//@Setter
@Data
@ApiModel(value = "用户实体类",description = "用于接口传输参数")
public class UserDto {
    @ApiModelProperty(value = "用户ID",example = "12",required = true)
    private String userId;
    @ApiModelProperty(value = "用户名",example = "jack",required = true)
    private String name;
}
