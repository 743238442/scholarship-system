package com.example.scholarship.exception;

/**
 * 业务逻辑异常类
 * 
 * @author System
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    private Integer errorCode;
    
    /**
     * 默认构造函数
     */
    public BusinessException() {
        super();
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param errorCode 错误码
     */
    public BusinessException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param cause 异常原因
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 构造函数
     * 
     * @param message 错误消息
     * @param errorCode 错误码
     * @param cause 异常原因
     */
    public BusinessException(String message, Integer errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    /**
     * 获取错误码
     * 
     * @return 错误码
     */
    public Integer getErrorCode() {
        return errorCode;
    }
    
    /**
     * 设置错误码
     * 
     * @param errorCode 错误码
     */
    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}