package com.example.scholarship.common;

/**
 * API响应封装类
 * 
 * @author System
 * @version 1.0.0
 */
public class ApiResponse {
    
    // 成功状态码
    public static final Integer SUCCESS_CODE = 200;
    // 失败默认状态码
    public static final Integer ERROR_CODE = 400;
    
    private Integer code;
    private String message;
    private Object data;
    private boolean success;
    
    /**
     * 构造函数
     */
    public ApiResponse() {
    }
    
    /**
     * 构造函数
     * 
     * @param code 响应码
     * @param message 响应消息
     * @param data 响应数据
     */
    public ApiResponse(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
        // 成功状态码判断更灵活
        this.success = code != null && code == SUCCESS_CODE;
    }
    
    /**
     * 构造函数
     * 
     * @param success 是否成功
     * @param message 响应消息
     */
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.code = success ? SUCCESS_CODE : ERROR_CODE;
        this.data = null;
    }
    
    /**
     * 成功响应
     * 
     * @param data 响应数据
     * @return 响应对象
     */
    public static ApiResponse success(Object data) {
        return new ApiResponse(SUCCESS_CODE, "操作成功", data);
    }
    
    /**
     * 带自定义消息的成功响应
     * 
     * @param message 自定义消息
     * @param data 响应数据
     * @return 响应对象
     */
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(SUCCESS_CODE, message, data);
    }
    
    /**
     * 错误响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return 响应对象
     */
    public static ApiResponse error(Integer code, String message) {
        return new ApiResponse(code, message, null);
    }
    
    /**
     * 默认错误响应
     * 
     * @param message 错误消息
     * @return 响应对象
     */
    public static ApiResponse error(String message) {
        return new ApiResponse(ERROR_CODE, message, null);
    }
    
    /**
     * 获取响应码
     * 
     * @return 响应码
     */
    public Integer getCode() {
        return code;
    }
    
    /**
     * 设置响应码
     * 
     * @param code 响应码
     */
    public void setCode(Integer code) {
        this.code = code;
    }
    
    /**
     * 获取响应消息
     * 
     * @return 响应消息
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * 设置响应消息
     * 
     * @param message 响应消息
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    /**
     * 获取响应数据
     * 
     * @return 响应数据
     */
    public Object getData() {
        return data;
    }
    
    /**
     * 设置响应数据
     * 
     * @param data 响应数据
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * 获取是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }
    
    /**
     * 设置是否成功
     * 
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
}