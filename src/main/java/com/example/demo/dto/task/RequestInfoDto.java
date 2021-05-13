package com.example.demo.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: JJJJ
 * @date:2021/5/12 17:03
 * @Description: TODO
 */
@ApiModel(value = "请求jenkins 数据传输对象")
@Data
public class RequestInfoDto {


    //请求的接口地址
    @ApiModelProperty(value="请求的接口地址，用于拼装命令",hidden=true)
    private String  requestUrl;
    //请求的服务器地址
    @ApiModelProperty(value="请求的服务器地址，用于拼装命令",hidden=true)
    private String  baseUrl;

    //文件服务器地址
    @ApiModelProperty(value="文件服务器地址，用于拼装命令",hidden=true)
    private String  fileServer;

    //token
    @ApiModelProperty(value="token信息，用于拼装命令",hidden=true)
    private String  token;
}
