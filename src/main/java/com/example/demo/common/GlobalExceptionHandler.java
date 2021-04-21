package com.example.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author: JJJJ
 * @date:2021/4/20 19:46
 * @Description: 全局自定义异常类
 */

@ControllerAdvice //表示为全局异常捕获类 使业务逻辑与异常处理剥离开
@ResponseBody // 将响应直接映射在http响应体上
@Slf4j // 日志配置注解
public class GlobalExceptionHandler {

    // 业务异常
    // @ExceptionHandler({ServiceException.class}) 指定只捕获业务异常
    @ExceptionHandler({ServiceException.class}) // 对异常进行统一处理 value指定捕获的异常类型
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public String serviceExceptionHandler(ServiceException ex){
        log.error(ex.getMessage());
        return resultFormat(ex);
    }

    // 其他异常
    // @ExceptionHandler({ServiceException.class})捕获非业务异常 ，业务异常继承了Exception，但是会先子后父进行捕获
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Exception ex){
        log.error(ex.getMessage());
        return resultFormat(ex);
    }
    // 系统错误
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String  exception(Throwable throwable){
        log.error("服务器异常"+ throwable);
        return resultFormat(throwable);
    }

    private <T extends Throwable> String resultFormat(T ex){
        log.error(ex.getMessage());
//        ResultDto resultDto = ResultDto.newInstance();
//        ResultDto.setAsFailure();
        if(ex instanceof  ServiceException){
//            ServiceException serviceException = (ServiceException)ex;
//            resultDto.setMessage(serviceException.getMessage());
            return "业务异常";
        }else if (ex instanceof Exception){
//            resultDto.setMessage("服务器不可用" + ex.getMessage());
            return "非业务异常";
        }
        return "系统错误";
    }
}
