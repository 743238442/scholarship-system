package com.example.scholarship.config;

import com.example.scholarship.entity.UserEntity;
import com.example.scholarship.repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 数据初始化配置
 * 应用启动时创建测试用户
 * 
 * @author System
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            // 短暂延迟以确保数据库初始化完成
            Thread.sleep(2000);
            
            // 创建管理员用户
            if (!userRepository.existsByUsername("admin")) {
                UserEntity admin = new UserEntity();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setEmail("admin@scholarship.edu.cn");
                admin.setPhone("13800000000");
                admin.setRole(UserEntity.UserRole.ADMIN);
                admin.setIsActive(true);
                userRepository.save(admin);
                System.out.println("管理员用户创建成功: admin / admin123");
            } else {
                System.out.println("管理员用户已存在，跳过创建");
            }

            // 创建学生用户
            if (!userRepository.existsByUsername("student")) {
                UserEntity student = new UserEntity();
                student.setUsername("student");
                student.setPassword(passwordEncoder.encode("student123"));
                student.setEmail("student@scholarship.edu.cn");
                student.setPhone("13900000000");
                student.setRole(UserEntity.UserRole.STUDENT);
                student.setIsActive(true);
                userRepository.save(student);
                System.out.println("学生用户创建成功: student / student123");
            } else {
                System.out.println("学生用户已存在，跳过创建");
            }

            // 创建新学生用户 - 张小明
            if (!userRepository.existsByUsername("zhangxm")) {
                UserEntity newStudent = new UserEntity();
                newStudent.setUsername("zhangxm");
                newStudent.setPassword(passwordEncoder.encode("zhangxm123"));
                newStudent.setEmail("zhangxm@scholarship.edu.cn");
                newStudent.setPhone("13900000001");
                newStudent.setRole(UserEntity.UserRole.STUDENT);
                newStudent.setIsActive(true);
                userRepository.save(newStudent);
                System.out.println("新学生用户创建成功: zhangxm / zhangxm123");
            } else {
                System.out.println("学生用户zhangxm已存在，跳过创建");
            }
            
            // 验证创建结果
            System.out.println("数据初始化完成，当前用户总数: " + userRepository.count());
            
        } catch (Exception e) {
            System.err.println("数据初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}