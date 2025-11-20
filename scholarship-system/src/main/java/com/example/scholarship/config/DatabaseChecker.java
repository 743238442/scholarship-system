package com.example.scholarship.config;

import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class DatabaseChecker {
    
    public static void main(String[] args) {
        // 启动Spring Boot应用以获取容器上下文
        String[] springArgs = {
            "--spring.main.web-application-type=none",
            "--spring.datasource.url=jdbc:h2:mem:scholarship;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
            "--spring.datasource.username=sa",
            "--spring.datasource.password=",
            "--spring.jpa.hibernate.ddl-auto=validate",
            "--spring.jpa.show-sql=false",
            "--spring.flyway.enabled=false"
        };
        
        ApplicationContext context = SpringApplication.run(com.example.scholarship.ScholarshipSystemApplication.class, springArgs);
        
        // 获取repository bean
        com.example.scholarship.repository.UserRepository userRepository = context.getBean(com.example.scholarship.repository.UserRepository.class);
        com.example.scholarship.repository.StudentRepository studentRepository = context.getBean(com.example.scholarship.repository.StudentRepository.class);
        
        System.out.println("=== 数据库内容检查 ===");
        
        // 检查所有用户
        System.out.println("\n--- 所有用户 ---");
        userRepository.findAll().forEach(user -> {
            try {
                // 使用反射获取字段值
                java.lang.reflect.Method getIdMethod = user.getClass().getMethod("getId");
                java.lang.reflect.Method getUsernameMethod = user.getClass().getMethod("getUsername");
                java.lang.reflect.Method getUserTypeMethod = user.getClass().getMethod("getUserType");
                java.lang.reflect.Method getStatusMethod = user.getClass().getMethod("getStatus");
                
                Object id = getIdMethod.invoke(user);
                Object username = getUsernameMethod.invoke(user);
                Object userType = getUserTypeMethod.invoke(user);
                Object status = getStatusMethod.invoke(user);
                
                System.out.println("用户ID: " + (id != null ? id.toString() : "NULL") + 
                                 ", 用户名: " + (username != null ? username.toString() : "NULL") + 
                                 ", 类型: " + (userType != null ? userType.toString() : "NULL") + 
                                 ", 状态: " + (status != null ? status.toString() : "NULL"));
            } catch (Exception e) {
                System.out.println("用户信息获取失败: " + e.getMessage());
            }
        });
        
        System.out.println("\n=== 检查完成 ===");
        System.exit(0);
    }
}