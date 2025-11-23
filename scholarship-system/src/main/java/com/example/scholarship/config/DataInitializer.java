package com.example.scholarship.config;

import com.example.scholarship.entity.User;
import com.example.scholarship.entity.Student;
import com.example.scholarship.entity.ScholarshipType;
import com.example.scholarship.repository.UserRepository;
import com.example.scholarship.repository.StudentRepository;
import com.example.scholarship.repository.ScholarshipTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private ScholarshipTypeRepository scholarshipTypeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("开始初始化数据...");
            
            // 短暂延迟以确保数据库初始化完成
            Thread.sleep(2000);
            
            // 创建管理员用户
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRealName("管理员");
            admin.setEmail("admin@example.com");
            admin.setUserType(User.UserType.ADMIN);
            admin.setStatus(User.UserStatus.ACTIVE);
            userRepository.save(admin);
            System.out.println("创建管理员用户: admin");
        } else {
            System.out.println("管理员用户已存在，跳过创建");
        }

        // 创建学生用户
        if (!userRepository.existsByUsername("student")) {
            User studentUser = new User();
            studentUser.setUsername("student");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setRealName("李小明");
            studentUser.setEmail("student@example.com");
            studentUser.setUserType(User.UserType.STUDENT);
            studentUser.setStatus(User.UserStatus.ACTIVE);
            studentUser = userRepository.save(studentUser);
        } else {
            System.out.println("学生用户已存在，跳过创建用户");
        }
        
        // 为学生用户创建或更新对应的Student记录
        User studentUser = userRepository.findByUsername("student").orElse(null);
        if (studentUser != null) {
            if (studentRepository.existsByStudentNo("20210001")) {
                System.out.println("学生记录已存在，跳过创建");
            } else {
                Student student = new Student();
                student.setStudentNo("20210001");
                student.setName("李小明");
                student.setGender(Student.Gender.MALE);
                student.setCollege("计算机科学与技术学院");

                student.setMajor("计算机科学与技术");
                student.setClazz("计科2021-1");
                student.setGrade("2021");
                student.setContact("13800000001");
                student.setEnrollmentDate(LocalDate.parse("2021-09-01"));
                student.setIsGraduated(false);
                student.setIdCard("110101200101010001");
                student.setStatus(Student.StudentStatus.ACTIVE);
                student.setStudyYears(4);
                student.setUser(studentUser);
                studentRepository.save(student);
                System.out.println("创建学生记录: 学号20210001, 姓名李小明");
            }
        }

            // 创建新学生用户 - 张小明
        if (!userRepository.existsByUsername("zhangxm")) {
            User zhangxmUser = new User();
            zhangxmUser.setUsername("zhangxm");
            zhangxmUser.setPassword(passwordEncoder.encode("zhangxm123"));
            zhangxmUser.setRealName("张小明");
            zhangxmUser.setEmail("zhangxm@example.com");
            zhangxmUser.setUserType(User.UserType.STUDENT);
            zhangxmUser.setStatus(User.UserStatus.ACTIVE);
            zhangxmUser = userRepository.save(zhangxmUser);
            
            // 为zhangxm用户创建对应的Student记录
            if (studentRepository.existsByStudentNo("20210002")) {
                System.out.println("学生记录已存在，跳过创建");
            } else {
                Student student = new Student();
                student.setStudentNo("20210002");
                student.setName("张小明");
                student.setGender(Student.Gender.MALE);
                student.setCollege("软件工程学院");

                student.setMajor("软件工程");
                student.setClazz("软工2021-1");
                student.setGrade("2021");
                student.setContact("13800000002");
                student.setEnrollmentDate(LocalDate.parse("2021-09-01"));
                student.setIsGraduated(false);
                student.setIdCard("110101200101010002");
                student.setStatus(Student.StudentStatus.ACTIVE);
                student.setStudyYears(4);
                student.setUser(zhangxmUser);
                studentRepository.save(student);
                System.out.println("创建学生记录: 学号20210002, 姓名张小明");
            }
        }
            
            // 初始化奖学金类型数据
            initScholarshipTypes();
            
            // 验证创建结果
            System.out.println("数据初始化完成，当前用户总数: " + userRepository.count());
            System.out.println("数据初始化完成！");
            
        } catch (Exception e) {
            System.err.println("数据初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 初始化奖学金类型数据
     */
    private void initScholarshipTypes() {
        System.out.println("开始初始化奖学金类型数据...");
        
        // 创建奖学金类型数据
        createScholarshipType("国家奖学金", new BigDecimal("8000.00"), "奖励特别优秀学生", "GPA≥3.8，综合排名前5%");
        createScholarshipType("国家励志奖学金", new BigDecimal("5000.00"), "奖励品学兼优的家庭经济困难学生", "GPA≥3.5，家庭经济困难，综合排名前10%");
        createScholarshipType("校级一等奖学金", new BigDecimal("3000.00"), "奖励学习成绩优异的学生", "GPA≥3.7，综合排名前15%");
        createScholarshipType("校级二等奖学金", new BigDecimal("2000.00"), "奖励学习成绩良好的学生", "GPA≥3.5，综合排名前25%");
        createScholarshipType("校级三等奖学金", new BigDecimal("1000.00"), "奖励学习成绩合格的学生", "GPA≥3.2，综合排名前40%");
        
        System.out.println("奖学金类型数据初始化完成");
    }
    
    private void createScholarshipType(String name, BigDecimal amount, String description, String eligibilityCriteria) {
        if (!scholarshipTypeRepository.existsByName(name)) {
            ScholarshipType type = new ScholarshipType();
            type.setName(name);
            type.setAmount(amount);
            type.setDescription(description);
            type.setEligibilityCriteria(eligibilityCriteria);
            scholarshipTypeRepository.save(type);
            System.out.println("已创建奖学金类型: " + name);
        } else {
            System.out.println("奖学金类型已存在: " + name);
        }
    }
}