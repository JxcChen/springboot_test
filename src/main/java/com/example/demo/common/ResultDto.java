package com.example.demo.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author: JJJJ
 * @date:2021/4/22 10:59
 * @Description: 统一返回对象
 */
@ApiModel(value = "基础响应返回对象")
public class ResultDto<T> implements Serializable {
    // 声明版本号
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "返回结果码 1 成功 0失败", required = true,example = "1")
    private Integer resultCode;
    @ApiModelProperty(value = "结果信息",required = true,example = "成功",allowableValues = "成功,失败")
    private String resultMsg = "";

    public Integer gewResultCode(){ return resultCode; }

    public static ResultDto newInstance(){ return new ResultDto();}
    // 将状态吗设置成成功
    public void setAsSuccess(){
        this.resultCode = 1;
    }
    // 将状态吗设置成失败
    public void setAsFail(){
        this.resultCode = 0;
    }

    @ApiModelProperty(value = "响应结果数据")
    private T data = null;

    // 执行成功响应
    public static ResultDto success(String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setAsSuccess();
        resultDto.setResultMsg(message);
        return resultDto;
    }
    // 执行成功响应
    public static <T> ResultDto<T> success(String message,T data){
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsSuccess();
        resultDto.setResultMsg(message);
        resultDto.setData(data);
        return resultDto;
    }

    // 执行失败响应 无对象信息
    public static ResultDto fail(String message){
        ResultDto resultDto = new ResultDto();
        resultDto.setAsFail();
        resultDto.setResultMsg(message);
        return resultDto;
    }
    // 执行失败响应
    public static <T> ResultDto<T> fail(String message,T data){
        ResultDto<T> resultDto = new ResultDto<>();
        resultDto.setAsFail();
        resultDto.setResultMsg(message);
        resultDto.setData(data);
        return resultDto;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }
}
