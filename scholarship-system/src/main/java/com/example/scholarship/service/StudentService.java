package com.example.scholarship.service;

import com.example.scholarship.entity.Student;
import java.util.Optional;

/**
 * 学生服务接口
 * 
 * @author System
 * @version 1.0.0
 */
public interface StudentService {

    /**
     * 根据用户ID查找学生信息及其关联的学业记录
     * @param userId 用户ID
     * @return 包含完整信息的Student对象
     */
    Optional<Student> findByUserId(Long userId);
    

}
