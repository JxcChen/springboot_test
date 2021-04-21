package com.example.demo.common;

/**
 * @author: JJJJ
 * @date:2021/4/20 15:49
 * @Description: 声明自定义异常
 */
public class ServiceException extends RuntimeException {
    // 指定版本  只是约定
    private static final long serialVersionUID = 1L;

    private String message;

    @Override
    public String getMessage(){return message;}


    public void setMessage(String message){
        this.message = message;
    }

    /**
     *
     * @param message 异常信息
     * @param throwable 具体运行异常类型
     */
    public ServiceException(final String message,Throwable throwable){
        super(message,throwable);
        this.message = message;
    }
    // 不进行异常抛出
    public ServiceException(final String message){
        this.message = message;
    }
    // 直接抛出异常
    public static void throwEx(String message){
        throw new ServiceException(message);
    }

}
