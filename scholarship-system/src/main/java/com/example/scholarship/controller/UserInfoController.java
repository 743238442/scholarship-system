package com.example.scholarship.controller;

import com.example.scholarship.entity.User;
import com.example.scholarship.entity.Student;
import com.example.scholarship.repository.UserRepository;
import com.example.scholarship.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 用户信息查询控制器
 * 用于验证数据库中的用户数据
 * 
 * @author System
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/debug")
public class UserInfoController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 查询所有用户信息
     */
    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查询所有用户
            Iterable<User> allUsers = userRepository.findAll();
            result.put("allUsers", allUsers);
            result.put("totalUsers", userRepository.count());
            result.put("message", "Successfully retrieved all users from database");
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("message", "Failed to retrieve users");
        }
        
        return result;
    }

    /**
     * 查询张小明的详细信息
     */
    @GetMapping("/zhangxm")
    public Map<String, Object> getZhangxmInfo() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 查找张小明的用户信息
            Optional<User> userOpt = userRepository.findByUsername("zhangxm");
            
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                result.put("user", user);
                
                // 查找对应的学生信息
                Optional<Student> studentOpt = studentRepository.findByUser(user);
                if (studentOpt.isPresent()) {
                    Student student = studentOpt.get();
                    result.put("student", student);
                } else {
                    result.put("student", "No student record found");
                }
                
                result.put("message", "张小明的完整信息已从数据库成功获取");
                result.put("databaseConfirmed", true);
                
            } else {
                result.put("message", "在数据库中未找到用户 zhangxm");
                result.put("databaseConfirmed", false);
            }
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("message", "查询张小明信息时出错");
        }
        
        return result;
    }

    /**
     * 验证数据库连接状态
     */
    @GetMapping("/db-status")
    public Map<String, Object> getDbStatus() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            long userCount = userRepository.count();
            long studentCount = studentRepository.count();
            
            result.put("userCount", userCount);
            result.put("studentCount", studentCount);
            result.put("databaseConnection", "Connected");
            result.put("message", "数据库连接正常，数据状态确认");
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("databaseConnection", "Disconnected");
            result.put("message", "数据库连接失败");
        }
        
        return result;
    }
}