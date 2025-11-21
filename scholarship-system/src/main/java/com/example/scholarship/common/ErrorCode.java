package com.example.scholarship.common;

/**
 * 错误码定义类
 * 错误码范围说明：
 * 1000-1999: 用户相关错误
 * 2000-2999: 学生相关错误
 * 3000-3999: 奖学金相关错误
 * 9000-9999: 系统级错误
 * 
 * @author System
 * @version 1.0.0
 */
public class ErrorCode {
    
    /**
     * 用户不存在错误码
     */
    public static final Integer USER_NOT_FOUND = 1001;
    
    /**
     * 用户不存在错误码（新名称）
     */
    public static final Integer USER_NOT_EXIST = 1001;
    
    /**
     * 用户权限不足错误码
     */
    public static final Integer USER_PERMISSION_DENIED = 1002;
    
    /**
     * 用户已存在错误码
     */
    public static final Integer USER_ALREADY_EXISTS = 1003;
    
    /**
     * 学生信息不存在错误码
     */
    public static final Integer STUDENT_NOT_FOUND = 2001;
    
    /**
     * 学生信息不存在错误码（新名称）
     */
    public static final Integer STUDENT_NOT_EXIST = 2001;
    
    /**
     * 学生信息已存在错误码
     */
    public static final Integer STUDENT_ALREADY_EXISTS = 2002;
    
    /**
     * 学生状态异常错误码
     */
    public static final Integer STUDENT_STATUS_INVALID = 2003;
    
    /**
     * 奖学金类型不存在错误码
     */
    public static final Integer SCHOLARSHIP_TYPE_NOT_FOUND = 3001;
    
    /**
     * 奖学金类型不存在错误码（新名称）
     */
    public static final Integer SCHOLARSHIP_TYPE_NOT_EXIST = 3001;
    
    /**
     * 重复申请错误码
     */
    public static final Integer DUPLICATE_APPLICATION = 3002;
    
    /**
     * 重复申请错误码（新名称）
     */
    public static final Integer APPLICATION_DUPLICATE = 3002;
    
    /**
     * 申请状态错误码
     */
    public static final Integer APPLICATION_STATUS_ERROR = 3003;
    
    /**
     * 申请已过期错误码
     */
    public static final Integer APPLICATION_EXPIRED = 3004;
    
    /**
     * 系统错误错误码
     */
    public static final Integer SYSTEM_ERROR = 9999;
    
    /**
     * 数据库错误错误码
     */
    public static final Integer DATABASE_ERROR = 9001;
    
    /**
     * 网络错误错误码
     */
    public static final Integer NETWORK_ERROR = 9002;
    
    /**
     * 参数校验错误错误码
     */
    public static final Integer PARAMETER_VALIDATION_ERROR = 9003;
}